/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.core.factory;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @ClassName PropertyThreadPoolFactory
 * @Description newland提供线程池工厂类
 * @Author wuxw
 * @Date 2021/1/14 21:04
 * @Version 1.0
 * add by wuxw 2021/1/14
 **/
public class PropertyThreadPoolFactory<T> {

    public static final int NEWLANDPROPERTY_DEFAULT_THREAD_NUM = 5;
    private Vector<Future<T>> futureList = null;
    ExecutorService executor = null;

    public static PropertyThreadPoolFactory getInstance() {
        return new PropertyThreadPoolFactory();
    }

    /**
     * 创建 线程池
     *
     * @param defaultThreadNum 线程数量
     * @return
     */
    public PropertyThreadPoolFactory<T> createThreadPool(int defaultThreadNum) {
        futureList = new Vector<>();
        executor = Executors.newFixedThreadPool(defaultThreadNum);
        return this;
    }


    public PropertyThreadPoolFactory submit(Callable<T> task) {
        Future<T> result = executor.submit(task);
        futureList.add(result);
        return this;
    }

    public List<T> get() {
        List<T> result = new LinkedList<>();
        for (Future<T> future :
                futureList) {
            try {
                result.add(future.get());
            } catch (Exception e) {
                result.add(null);
            }

        }
        return result;
    }

    public void stop() {
        executor.shutdown();
    }
}
