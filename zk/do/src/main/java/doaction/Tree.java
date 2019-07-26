package doaction;


import org.springframework.util.StringUtils;

import java.util.*;


public abstract class Tree<T> {

   //最大的循环次数
    private  int MaxCount;

    //存储原始的列表数据
    private List<T> list;

    private String rootPId;

    private String rootId;
    //存储游离的节点 key为节点的ID
    private Map<String, T> detachedMap = new HashMap<String, T>();

    //树上的叶子节点 key为节点的ID
    private Map<String, T> leafMap = new HashMap<String, T>();


    public Tree(List<T> srcList) {
        this.list = srcList;
        this.MaxCount =20;
    }


    public Tree(List<T> srcList,int maxDeep) {
        this.list = srcList;
        this.MaxCount = maxDeep;
    }



    private void setRootId(String rootPId){
        this.rootId =rootPId;
    }
    private void setRootPId(String rootPId) {
        this.rootPId = rootPId;
    }

    public List<T> buildWithRoot(String rootID) {
        setRootId(rootID);
        if(StringUtils.isEmpty(rootID)){
            return null;
        }
        return build();
    }

    /**
     * 当前端需要同时返回多个根节点时,使用此方法
     * @param ParentRootID
     * @return
     */
    public List<T> buildWithParentRoot(String ParentRootID) {
        setRootPId(ParentRootID);
        if(StringUtils.isEmpty(ParentRootID)){
            return null;
        }
        return build();
    }

    public abstract T todo(T parent,T node,int level);

    private List<T> build() {

        // 找到第一个需要展示的第一层级的所有元素
        List<T> roots = getFirstLevelMenuParentID();
        int count=1;
        for (T m : list) {
            String nodeId = getId(m);
            // String pid=getPId(m);
            // 设置第一层级的子元素
            if (contains(m, roots)) {
                todo(null,m,count);
                leafMap.put(nodeId, m);
            } else {
                detachedMap.put(nodeId, m);
            }
        }

        while (detachedMap.size() > 0) {
            count++;
            Object[] keys = (Object[]) leafMap.keySet().toArray();
            // 如果叶子节点的key等于游离节点的key，则表示游离节点属于该叶子节点的子节点、叶子节点上升为父节点，从map中删除
            for (Object key : keys) {
                T oldLeaf = leafMap.get(key.toString());
                List<T> children = findChild(oldLeaf);
                if (!children.isEmpty()) {
                    associate(oldLeaf, children,count);
                }
            }
            if(count>=MaxCount){
                break;
            }
        }

        return roots;
    }


    private void associate(T oldLeaf, List<T> children, int count) {
        for (T child : children) {
            String childID = getId(child);
            String parentID = getId(oldLeaf);
            // 父子相认
            setChildren(oldLeaf, child);
            todo(oldLeaf,child,count);
            // 孩子纳入年轻代范围,可挂孩子
            leafMap.put(childID, child);
            // 父亲已老
            leafMap.remove(parentID);
            // 孩子已不在游离
            detachedMap.remove(childID);
        }
    }

    /**
     * 根据父亲，在游离节点中找孩子
     */
    private List<T> findChild(T oldLeaf) {
        List<T> children = new ArrayList<T>();
//		String pid = getId(oldLeaf);
        for (Iterator<T> iterator = detachedMap.values().iterator(); iterator
                .hasNext();) {
            T child = (T) iterator.next();
//			System.out.println("parent ID:" + pid + "==========child Pid:"
//					+ getPId(child));
            if (isChildren(oldLeaf, child)) {
                children.add(child);
            }
        }
        return children;
    }

    protected List<T> getFirstLevelMenuParentID() {
        List<T> ids = new ArrayList<T>();
        for (T menus : list) {
            if (isRoot(menus)) {
                ids.add(menus);
            }
        }
        return ids;
    }

    protected boolean contains(T node, List<T> plist) {
        for (T root1 : plist) {
            if (getId(root1).equals(getId(node))) {
                return true;
            }
        }
        return false;
    }
    protected boolean isRoot(T node){
        if (node != null && getPId(node).equals(rootPId)) {
            return true;
        }
        if (node != null && getId(node).equals(rootId)) {
            return true;
        }
        return false;
    }

    protected abstract void setChildren(T father, T child);

    protected abstract boolean isChildren(T p, T c);

    protected abstract String getPId(T node);

    protected abstract String getId(T node);
}
