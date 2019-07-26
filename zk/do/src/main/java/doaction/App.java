package doaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Hello world!
 *
 */

public class App 
{

    private static final int THREADS_SIZE = 1;

    private static final int CAPACITY = 1;

public static void main(String[] args) throws Exception {

    List<Menus> list = new ArrayList<>();
    MenuTree tree = new MenuTree(list,6);
    List<Menus> resultList = tree.buildWithParentRoot("");

// 创建线程池。线程池的"最大池大小"和"核心池大小"都为1(THREADS_SIZE)，"线程池"的阻塞队列容量为1(CAPACITY)。

    ThreadPoolExecutor pool = new ThreadPoolExecutor(THREADS_SIZE, THREADS_SIZE, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(CAPACITY));

// 设置线程池的拒绝策略为"丢弃"

    pool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

// 新建10个任务，并将它们添加到线程池中。

    for(int i = 0; i < 10; i++){

    Runnable myrun = new MyRunnable("task-"+i);

    pool.execute(myrun);

    }


    // 关闭线程池

    pool.shutdown();

}



}

class MyRunnable implements Runnable,Comparable{

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    private String name;

        public MyRunnable(String name) {

            this.name = name;

        }

        @Override
        public void run() {

            try {

            System.out.println(this.name + " is running.");

            Thread.sleep(100);

            } catch (Exception e) {

            e.printStackTrace();

            }

        }

}
