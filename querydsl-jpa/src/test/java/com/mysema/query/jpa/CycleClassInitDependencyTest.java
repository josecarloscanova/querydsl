package com.mysema.query.jpa;

import org.junit.Test;

import com.mysema.query.jpa.impl.JPAQuery;

public class CycleClassInitDependencyTest {

    @Test(timeout = 2000)
    public void test() throws InterruptedException {

        // If class is loaded before hand it will work for example:
        // System.out.println(Ops.DateTimeOps.DATE);

        // each thread wants
        Thread t1 = new Thread(new LoadClassRunnable("com.mysema.query.types.OperatorImpl"));
        Thread t2 = new Thread(new LoadClassRunnable("com.mysema.query.types.Ops"));
        t1.start();
        t2.start();

        // trying to create anything depending on Ops class for instance:
        // JPAQuery -> JPQLTemplates -> Ops
        new JPAQuery();

    }

    public class LoadClassRunnable implements Runnable {

        private String classToLoad;

        public LoadClassRunnable(String classToLoad) {
            this.classToLoad = classToLoad;
        }

        public void run() {
            try {
                Class.forName(classToLoad);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

}