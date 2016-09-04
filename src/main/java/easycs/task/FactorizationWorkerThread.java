package easycs.task;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Mmuzafarov
 */
public class FactorizationWorkerThread implements Callable<List<Integer>>, AutoCloseable {
    private final static Log logger = LogFactory.getLog(FactorizationWorkerThread.class);

    private final Integer number;
    private final List<Integer> threadResult;

    public FactorizationWorkerThread(Integer number) {
        this.number = number;
        threadResult = Lists.newArrayList();
    }

    protected void recursiveCalculation(int n, int k) {
        // k - additional parameter with start-recursion value
        if (k > n / 2) {
            threadResult.add(n);
            return;
        }
        // Step of recursion / recursive condition
        if (n % k == 0) {
            threadResult.add(k);
            recursiveCalculation(n / k, k);
        } else {
            recursiveCalculation(n, ++k);
        }
    }

    @Override
    public List<Integer> call() throws Exception {
        recursiveCalculation(number, 2);
        return threadResult;
    }

    @Override
    public void close() throws Exception {
        Thread.currentThread().interrupt();
    }
}
