package zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
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

    public static void main(String[] args) throws Exception{
       // zkClient();
        //curatorClient();
        System.out.println(test());

    }

    public static int test()throws Exception{
        try{
            System.out.println("try");
            return 1;

        }catch(Exception e){
            System.out.println("catch");
            return 2;
        }
        finally{
            System.out.println("finally");
            return 3;
        }
    }

    public static void curatorClient() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 5);
        CuratorFramework client =  CuratorFrameworkFactory.builder()
                .connectString("47.106.86.169:2181,47.106.86.169:2182,47.106.86.169:2183")
                .sessionTimeoutMs(30000).connectionTimeoutMs(15000)
                .retryPolicy(retryPolicy)
                //.namespace("curatorTest")
                .build();
        client.start();

        // 判断节点是否存在，存在则先删除节点
        Stat test1Stat = client.checkExists().forPath("/curatorTest/test1");
        if (null != test1Stat) {
            client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(-1).forPath("/curatorTest/test1");
        }

        // 创建节点
        String test1Data = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/curatorTest/test1", "test1DataV1".getBytes());

        // 获取节点信息
        test1Stat = new Stat();
        byte[] test1DataBytes = client.getData().storingStatIn(test1Stat).forPath("/curatorTest/test1");
        System.out.println("test1 stat: " + test1Stat);
        System.out.println("test1 data: " + new String(test1DataBytes));

        // 更新节点数据
        test1Stat = client.setData()
                .withVersion(-1)
                .forPath("/curatorTest/test1", "test1DataV2".getBytes());
        System.out.println("test1 stat: " + test1Stat);

        // 获取所有子节点
        Stat childStat = new Stat();
        List<String> childs = client.getChildren().storingStatIn(childStat).forPath("/curatorTest");
        System.out.println("curatorTest childs: " + childs);

        //        client.delete()
        //                .guaranteed()
        //                .withVersion(-1)
        //                .inBackground(((client1, event) -> {
        //                    System.out.println(event.getPath() + ", data=" + event.getData());
        //                    System.out.println("event type=" + event.getType());
        //                    System.out.println("event code=" + event.getResultCode());
        //                }))
        //                .forPath("/curatorTest/test1");

        // 缓存节点
        NodeCache nodeCache = new NodeCache(client, "/curatorTest/test1");
        nodeCache.start(true);
        nodeCache.getListenable().addListener(() -> {
            System.out.println("NodeCache:");
            ChildData childData = nodeCache.getCurrentData();
            if (null != childData) {
                System.out.println("path=" + childData.getPath() + ", data=" + new String(childData.getData()) + ";");
            }
        });


        // 缓存子节点
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/curatorTest", true);
        // startMode为BUILD_INITIAL_CACHE，cache是初始化完成会发送INITIALIZED事件
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        System.out.println(pathChildrenCache.getCurrentData().size());
        pathChildrenCache.getListenable().addListener(((client1, event) -> {
            ChildData data = event.getData();
            switch (event.getType()) {
                case INITIALIZED:
                    System.out.println("子节点cache初始化完成(StartMode为POST_INITIALIZED_EVENT的情况)");
                    System.out.println("INITIALIZED: " + pathChildrenCache.getCurrentData().size());
                    break;
                case CHILD_ADDED:
                    System.out.println("添加子节点，path=" + data.getPath() + ", data=" + new String(data.getData()));
                    break;
                case CHILD_UPDATED:
                    System.out.println("更新子节点，path=" + data.getPath() + ", data=" + new String(data.getData()));
                    break;
                case CHILD_REMOVED:
                    System.out.println("删除子节点，path=" + data.getPath());
                    break;
                default:
                    System.out.println(event.getType());
            }
        }));

        Thread.sleep(20000000);
    }



    public static void zkClient() {
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
