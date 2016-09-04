package easycs.task;

import com.google.common.collect.Queues;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Mmuzafarov
 */
public class MonitorWorkerThread implements Runnable {
    private static final Log logger = LogFactory.getLog(MonitorWorkerThread.class);
    private BlockingQueue<Runnable> blockingQueue = Queues.newArrayBlockingQueue(100);

    @Override
    public void run() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 5000, TimeUnit.MILLISECONDS, blockingQueue);

    }
}
