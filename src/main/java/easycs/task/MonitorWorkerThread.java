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
public class MonitorWorkerThread extends Thread {
    private static final Log logger = LogFactory.getLog(MonitorWorkerThread.class);

    private MonitorWorkerThread() {
    }

    private static class MonitorWorkerHolder {
        private static final MonitorWorkerThread HOLDER = new MonitorWorkerThread();
    }

    public static MonitorWorkerThread getMonitorInstance() {
        return MonitorWorkerHolder.HOLDER;
    }

    @Override
    public void run() {

    }

}
