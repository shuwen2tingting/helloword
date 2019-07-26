package doaction;

import lombok.Data;

import java.util.List;

@Data
public class Menus {
    private Long moduleId;
    private Long parentId;
    private int sort;
    List<Menus>  childModules;
}
