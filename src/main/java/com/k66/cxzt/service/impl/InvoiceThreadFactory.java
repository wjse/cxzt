package com.k66.cxzt.service.impl;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class InvoiceThreadFactory implements ThreadFactory {

    private String name;

    private final ThreadGroup group;

    private final AtomicInteger threadNum = new AtomicInteger(0);

    public InvoiceThreadFactory(String name) {
        SecurityManager s = System.getSecurityManager();
        this.group = (s != null) ? s.getThreadGroup()
                   : Thread.currentThread().getThreadGroup();
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group , r , String.format("%s%s" , name , threadNum.getAndIncrement()) , 0);

        if(t.isDaemon()){
            t.setDaemon(false);
        }

        if(t.getPriority() != Thread.NORM_PRIORITY){
            t.setPriority(Thread.NORM_PRIORITY);
        }

        return t;
    }
}
