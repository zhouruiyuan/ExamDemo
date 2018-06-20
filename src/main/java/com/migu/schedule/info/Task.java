package com.migu.schedule.info;

/**
 * 任务类
 * @author ryzhou
 *
 */
public class Task {
    private int taskId;
    private int consumption;
    private TaskInfo taskInfo;
    
    public Task(int taskId,int consumption,TaskInfo taskInfo){
    	this.taskId=taskId;
    	this.consumption=consumption;
    	this.taskInfo=taskInfo;
    }
    public TaskInfo getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

	public int getConsumption() {
		return consumption;
	}

	public void setConsumption(int consumption) {
		this.consumption = consumption;
	}

	public int getTaskId(){  return taskId; }

    public void setTaskId(int taskId)
    {
        this.taskId = taskId;
    }
    @Override
    public String toString()
    {
        return "Task [taskId=" + taskId + ", consumption=" + consumption + ", taskInfo=" + taskInfo+"]";
    }


}
