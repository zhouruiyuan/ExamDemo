package com.migu.schedule;


import com.migu.schedule.constants.ReturnCodeKeys;
import com.migu.schedule.info.Task;
import com.migu.schedule.info.TaskInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
*类名和方法不能修改
 */
public class Schedule {

    Map<Integer,List<Task>> nodes;
    Queue<Task> que=new ConcurrentLinkedQueue<Task>();
    
    public int init() {
    	nodes=new HashMap<Integer,List<Task>>();
    	que.clear();
        return ReturnCodeKeys.E001;
    }


    public int registerNode(int nodeId) {
    	if(nodeId<1){
    		return ReturnCodeKeys.E004;
    	}
    	if(nodes.containsKey(nodeId)){
    		return ReturnCodeKeys.E005;
    	}
    	nodes.put(nodeId, null);
        return ReturnCodeKeys.E003;
    }

    public int unregisterNode(int nodeId) {
    	if(nodeId<1){
    		return ReturnCodeKeys.E004;
    	}
    	for(Integer key:nodes.keySet()){
    		if(key==nodeId){
    			List<Task> task=nodes.get(key);
    			if(task!=null){
        			for(Task tinfo :task){
        				addTask(tinfo.getTaskId(),tinfo.getConsumption());
        			}
    			}
    			nodes.remove(key);
    			return ReturnCodeKeys.E006;
    		}
    	}
        return ReturnCodeKeys.E007;
    }


    public int addTask(int taskId, int consumption) {
    	if(taskId<=0){
            return ReturnCodeKeys.E009;
    	} 	
    	Iterator<Task> queTask=que.iterator();
    	Task temp;
        while(queTask.hasNext()){
        	temp=queTask.next();
        	if(temp!=null&&temp.getTaskId()==taskId){
        		return ReturnCodeKeys.E010;
        	}
        }
    	List<Task> tempList;
        for(Integer key:nodes.keySet()){        	
        	tempList=nodes.get(key);
        	if(tempList!=null){
            	for(Task temp1:tempList){
            		if(temp1!=null&&temp1.getTaskId()==taskId){
            			return ReturnCodeKeys.E010;
            		}
            	}
        	}

        }
    	TaskInfo tainfo=new TaskInfo();
    	Task task=new Task(taskId,consumption,tainfo);
    	que.add(task);
        return ReturnCodeKeys.E008;
    }


    public int deleteTask(int taskId) {
    	if(taskId<=0){
            return ReturnCodeKeys.E009;
    	}
    	Iterator<Task> queTask=que.iterator();
    	Task temp;
    	List<Task> tempList;
        while(queTask.hasNext()){
        	temp=queTask.next();
        	if(temp!=null&&temp.getTaskId()==taskId){
        		que.remove(temp);
        		return ReturnCodeKeys.E011;
        	}
        }
        for(Integer key:nodes.keySet()){        	
        	tempList=nodes.get(key);
        	if(tempList!=null){
        	for(Task temp1:tempList){
        		if(temp1!=null&&temp1.getTaskId()==taskId){
        			tempList.remove(temp1);
        			nodes.put(key, tempList);
        			return ReturnCodeKeys.E011;
        		}
        	}
        	}
        }
        return ReturnCodeKeys.E012;
    }


    public int scheduleTask(int threshold) {
    	List<Task> tempList;
    	int num=0;
    	List<Integer> nodeIdList=new ArrayList<Integer>();
        for(Integer key:nodes.keySet()){        	
        	tempList=nodes.get(key);
        	num++;
        	nodeIdList.add(key);
        	if(tempList!=null){
        	for(Task temp1:tempList){
        		if(temp1!=null&&temp1.getTaskId()>0){
        			addTask(temp1.getTaskId(),temp1.getConsumption());
        		}
        	}
        	}
        }
        
    	if(!que.isEmpty()){
    		distribution(num,nodeIdList);
    		return ReturnCodeKeys.E013;
    	}
        return ReturnCodeKeys.E014;
    }


    public int queryTaskStatus(List<TaskInfo> tasks) {
    	List<Task> tempList;
    	List<Integer> com=new ArrayList<Integer>();
    	tasks.clear();
        for(Integer key:nodes.keySet()){
        	tempList=nodes.get(key);
        	if(tempList!=null){
        	for(Task temp1:tempList){
        		if(temp1!=null&&temp1.getTaskId()>0&&temp1.getTaskInfo()!=null){
        			TaskInfo info=new TaskInfo();
        			info.setTaskId(temp1.getTaskId());
        			info.setNodeId(temp1.getTaskInfo().getNodeId());
        			tasks.add(info);
        		}
        	}
        	}
        }
        if(!tasks.isEmpty()){
        	
        	return ReturnCodeKeys.E015;
        }
        return ReturnCodeKeys.E016;
    }

    public void distribution(int num,List<Integer> nodeIdList){
    	Iterator<Task> itera=que.iterator();
    	List<Integer> com=new ArrayList<Integer>();
    	List<Integer> com1=new ArrayList<Integer>();
    	int comz=0;
    	Map<Integer,Integer> map=new HashMap<Integer,Integer>();
    	while(itera.hasNext()){	
    		Task task=itera.next();
    		if(task!=null&&task.getTaskId()>0&&task.getConsumption()>0){
    			map.put(task.getTaskId(), task.getConsumption());
    			com.add(task.getConsumption());
    			com1.add(task.getTaskId());
    			comz++;
    		}
    		
    	}    	   	
    	Collections.sort(com1);
    	if(comz%num==0&&com.get(0).equals(com.get(com.size()-1))){
    		int shan=comz/num;
    		Collections.sort(nodeIdList);
    		int j=0;
    		for(int nodeid:nodeIdList){
    			
        		List<Task> temptask=nodes.get(nodeid);
        		if(temptask==null){
        			temptask=new ArrayList<Task>();
        		}
        			for(int i=0;i<shan;i++){
        				TaskInfo info=new TaskInfo();
        				info.setNodeId(nodeid);
        				info.setTaskId(com1.get(0+i));
        				temptask.add(new Task(com1.get(j+i),com.get(0),info));
        			}
        			nodes.put(nodeid,temptask);
               j=j+shan;
    		}
    	}
    	/*
    	Iterator<Task> itera=que.iterator();
    	List<Integer> com=new ArrayList<Integer>();
    	int comz=0;
    	Map<Integer,Integer> map=new HashMap<Integer,Integer>();
    	while(itera.hasNext()){
    		Task task=itera.next();
    		if(task!=null&&task.getTaskId()>0&&task.getConsumption()>0){
    			map.put(task.getTaskId(), task.getConsumption());
    			com.add(task.getConsumption());
    			comz++;
    		}
    		
    	}
    	Collections.sort(com);
    	int cha=comz/num;
    	int yu=comz%num;
    	int[] feipei=new int[num];
        for(int i=0;i<yu;i++){
        	feipei[i]=cha+1;
        }
        for(int i=yu;i<num;i++){
        	feipei[i]=cha;
        }
        for(int j=0;j<num;j++){
        	int temp=0;
           while(temp==feipei[j]){
        	   
           }
        }
    	int[][] comcz=new int[num][com.size()];
    	
    */}
    
}
