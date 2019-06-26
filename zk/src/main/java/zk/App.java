package zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.NIOServerCnxn;
import org.apache.zookeeper.server.ServerCnxn;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String PARENT_PATH = "/zkClientTest";

    private static final String CHILD_PATH = "/zkClientTest/childNodeTest";

    private static final String IDENTITY = "zhangsan:123456";

    public static void main(String[] args) {
        try {
            Watcher defaultWatcher = new Watcher() {
                public void process(WatchedEvent event) {
                    System.out.println("-------"+event);
                }
            };

            Watcher childrenWatcher = new Watcher() {
                public void process(WatchedEvent event) {
                    System.out.println("-------"+event);
                }
            };

            Watcher parentWatcher = new Watcher() {
                public void process(WatchedEvent event) {
                    System.out.println("-------"+event);
                }
            };

            // 创建会话
            ZooKeeper client = new ZooKeeper("47.106.86.169:2181,47.106.86.169:2182,47.106.86.169:2183", 30000, defaultWatcher);

            while(!client.getState().isAlive()){
                TimeUnit.SECONDS.sleep(1);
            }

            client.addAuthInfo("digest", IDENTITY.getBytes());

            Stat stat = client.exists(PARENT_PATH, false);
            if (null != stat) {
                client.delete(PARENT_PATH, -1);
            }

            // 创建节点，临时节点不能有子节点，所以父节点是持久节点
            client.create(PARENT_PATH, "zkClientTestData_v1".getBytes(), getAcl(), CreateMode.PERSISTENT);

            // 创建子节点
            client.create(CHILD_PATH, "childNodeData_v1".getBytes(), getAcl(), CreateMode.EPHEMERAL);

            // 获取子节点信息
            Stat childStat = new Stat();
            List<String> childs = client.getChildren(PARENT_PATH, childrenWatcher, childStat);
            System.out.println(PARENT_PATH + "'s childs:" + childs);
            System.out.println(PARENT_PATH + "'s stat:" + childStat);

            Thread.sleep(1000);

            // 获取父节点数据
            Stat parentStat = new Stat();
            byte[] parentData = client.getData(PARENT_PATH, parentWatcher, parentStat);
            System.out.println(PARENT_PATH + "'s data: " + new String(parentData));
            System.out.println(PARENT_PATH + "'s stat: " + parentStat);

            Thread.sleep(1000);

            // 设置子节点数据
            childStat = client.setData(CHILD_PATH, "childNodeData_v2".getBytes(), -1);
            System.out.println(CHILD_PATH + "'s stat:" + childStat);
            byte[] childData = client.getData(CHILD_PATH, false, childStat);
            System.out.println(CHILD_PATH + "'s data:" + new String(childData));

            Thread.sleep(1000);

            // 删除子节点
            client.delete(CHILD_PATH, -1);
            // 判断子节点是否存在
            childStat = client.exists(CHILD_PATH, false);
            System.out.println(CHILD_PATH + " is exist: " + (childStat != null));

            client.delete(PARENT_PATH, -1);

            client.close();

            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ACL格式为：schema：id：permission
     * @return
     */
    private static List<ACL> getAcl() throws Exception {
        List<ACL> acls = new ArrayList<ACL>();

        // 指定schema
        String scheme = "auth";
        // 指定id
        // String identity = "zhangsan:123456";
        Id id = new Id(scheme, DigestAuthenticationProvider.generateDigest(IDENTITY));

        // Perms.ALL的权限为crdwa
        ACL acl = new ACL(ZooDefs.Perms.ALL, id);

        acls.add(acl);

        return acls;
    }
}
