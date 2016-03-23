package com.github.flowengine.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.github.flowengine.engine.FlowEngine;
import com.github.flowengine.util.Listener;
import com.github.flowengine.util.Listenerable;

/**
 * 流程执行的上下文
 * 
 * @author badqiu
 * 
 */
public class FlowContext implements Serializable {

	private transient ExecutorService executorService;
	private transient FlowEngine flowEngine;
	private Map params = new HashMap(); // 流程参数
	private Flow flow; // 流程
	private Map context = new HashMap(); //保存上下文内容
//	private List<String> visitedtaskIds = new ArrayList<String>(); //已经访问过的流程任务节点
	
	private transient Listenerable<FlowContext> listenerable = new Listenerable<FlowContext>();
	
	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}
	
	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

//	public List<String> getVisitedtaskIds() {
//		return visitedtaskIds;
//	}
//
//	public void setVisitedtaskIds(List<String> visitedtaskIds) {
//		this.visitedtaskIds = visitedtaskIds;
//	}

	public FlowEngine getFlowEngine() {
		return flowEngine;
	}

	public void setFlowEngine(FlowEngine flowEngine) {
		this.flowEngine = flowEngine;
	}
	
//	public void addVisitedtaskId1(String taskId) {
//		getVisitedtaskIds().add(taskId);
//		notifyListeners();
//	}
	
	
	
	public void notifyListeners() {
		listenerable.notifyListeners(this, null);
	}

	public Map getContext() {
		return context;
	}

	public void setContext(Map context) {
		this.context = context;
	}

	public void addListener(Listener<FlowContext> t) {
		listenerable.addListener(t);
	}
	
	public boolean awaitTermination() throws InterruptedException {
		return awaitTermination(9999999,TimeUnit.DAYS);
	}
	
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		ExecutorService executorService = getExecutorService();
		executorService.shutdown();
		return executorService.awaitTermination(timeout, unit);
	}
	
	//shut down untill all tasks have been finished
	public boolean awaitTerminationAfterAllFinish(long timeout, TimeUnit unit) throws InterruptedException {
		ThreadPoolExecutor tpe = (ThreadPoolExecutor)getExecutorService();
		int nodeSize = flow.getNodes().size();
		while(tpe.getCompletedTaskCount() < nodeSize){
			try {
				Thread.sleep(1000*10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		tpe.shutdown();
		return tpe.awaitTermination(timeout, unit);
	}
}
