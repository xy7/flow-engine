package com.github.flowengine.engine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.Assert;

import com.github.flowengine.model.Flow;
import com.github.flowengine.model.FlowContext;
import com.github.flowengine.model.FlowTask;
import com.github.flowengine.model.SystemOutTaskExecutor;

public class FlowEngineTest {

	@Test
	public void test() throws InterruptedException{
		
		Flow flow = new Flow();
		flow.setFlowId("flow_id0");
		flow.setMaxParallel(1);

		String depends = "";
		for(int i=0;i<1;i++){
			FlowTask task = new FlowTask("task_root_" + i);
			task.setScriptType(SystemOutTaskExecutor.class);
			flow.addNode(task);
			depends += ",task_root_" + i;
		}
		
		depends = depends.substring(1);
		for(int i=0;i<1;i++){
			FlowTask leaf = new FlowTask("task_leaf_" + i);
			leaf.setScriptType(SystemOutTaskExecutor.class);
			leaf.setDepends(depends);
			flow.addNode(leaf);
		}

		flow.init();
		flow.checkCircuit();
		System.out.println("flow:\n" + flow + "\n------");
		Map params = new HashMap();
		
		FlowEngine flowEngine = new FlowEngine();
		FlowContext context = flowEngine.exec(flow, params);
		
//		FlowContext context = new FlowContext();
//		context.setParams(params);
//		Integer maxParallel = flow.getMaxParallel();
//		Assert.isTrue(maxParallel > 0,"maxParallel > 0 must be true");
//		ExecutorService es = Executors.newFixedThreadPool(maxParallel,new CustomizableThreadFactory("flow-"+flow.getFlowId()+"-"));
//		context.setExecutorService(es);
//		context.setFlow(flow);
//		context.setFlowEngine(flowEngine);
//		FlowTask.execAllWithoutWait(context, false, true, flow.getNoDependNodes());
		
		//等待流程执行完成
		context.awaitTerminationAfterAllFinish(1, TimeUnit.SECONDS);
	}

}
