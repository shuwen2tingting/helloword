package doaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class MenuTree extends Tree<Menus> {

    public MenuTree(List<Menus> srcList) {
        super(srcList);
    }

    public MenuTree(List<Menus> srcList, int maxDeep) {
        super(srcList, maxDeep);
    }

//	@Override
//	protected boolean isRoot(Menus node) {
//		if (node != null && node.getParentId().longValue() == 2) {
//			return true;
//		}
//		return false;
//	}

    @Override
    protected boolean isChildren(Menus p, Menus c) {
        return p.getModuleId().longValue() == c.getParentId().longValue();
    }

    @Override
    protected void setChildren(Menus father, Menus child) {
        List<Menus> children = null;
        if (father.getChildModules() != null) {
            children = father.getChildModules();
            children.add(child);
        } else {
            children = new ArrayList<Menus>();
            children.add(child);
        }
        children.sort(new Comparator<Menus>() {
            @Override
            public int compare(Menus o1, Menus o2) {
                return o1.getSort() - o2.getSort();
            }
        });
        father.setChildModules(children);
    }

    @Override
    protected String getPId(Menus node) {
        return node.getParentId().longValue() + "";
    }

    @Override
    protected String getId(Menus node) {
        return node.getModuleId().longValue() + "";
    }

    @Override
    public Menus todo(Menus parent, Menus node, int level) {
        return null;
    }

}
