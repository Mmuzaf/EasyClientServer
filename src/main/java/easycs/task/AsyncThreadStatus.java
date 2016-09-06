package easycs.task;

/**
 * Indicates the current status of the task. Each status will be set only once
 * during the lifetime of a task.
 */
public enum AsyncThreadStatus {
    PENDING,
    RUNNING,
    FINISHED,
}
