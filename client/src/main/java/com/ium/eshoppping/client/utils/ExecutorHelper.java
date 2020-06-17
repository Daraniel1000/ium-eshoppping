package com.ium.eshoppping.client.utils;

import javafx.application.Platform;

import java.util.concurrent.*;

public class ExecutorHelper
{
    /**
     * End ExecutorService peacefully
     *
     * @param service service to end
     * @param timeout time in seconds after we give up waiting
     */
    public static void shutdownExecutor(ExecutorService service, long timeout)
    {
        service.shutdown();
        try
        {
            if(!service.awaitTermination(timeout,
                                         TimeUnit.SECONDS)) //wait for all threads to terminate, if timeout abort and ignore
            {
                service.shutdownNow();
            }
        }
        catch(InterruptedException e)
        {
            service.shutdownNow();
        }
    }

    /**
     * End ExecutorService abruptly
     * @param service service to end
     * @param timeout time in seconds after we give up waiting
     * @throws IllegalThreadStateException when waiting timed out, which means some threads are still running
     */
    public static void abortExecutor(ExecutorService service, long timeout) throws IllegalThreadStateException
    {
        service.shutdownNow();
        try
        {
            if(!service.awaitTermination(timeout,
                                         TimeUnit.SECONDS)) //wait for all threads to terminate, if timeout throw exception
                throw new IllegalThreadStateException("Some thread(s) still running");
        }
        catch(InterruptedException e)
        {
            throw new IllegalThreadStateException("Some thread(s) still running");
        }
    }

    /**
     * Wait for all previous Runnables added to runLater to finish
     */
    public static void waitForRunLater() throws InterruptedException
    {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();
    }

    public static ExecutorService newRejectingSingleThreadExecutor()
    {
        return new ThreadPoolExecutor(1, 1, 1L, TimeUnit.SECONDS, new SynchronousQueue<>(),
                                      new ThreadPoolExecutor.DiscardPolicy());
    }
}
