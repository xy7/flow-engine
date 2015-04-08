package com.duowan.flowengine.engine.task;

import java.util.Map;

import com.duowan.flowengine.engine.TaskExecutor;
import com.duowan.flowengine.model.FlowContext;
import com.duowan.flowengine.model.FlowTask;

/**
 * 通过agent执行的TaskExecutor
 * @author badqiu
 *
 */
public class AgentTaskExecutor implements TaskExecutor{

	@Override
	public void exec(FlowTask task, FlowContext flowContext) throws Exception {
		String agentUrl = task.getExecAgent();
		Agent agent = new Agent(agentUrl);
		agent.exec(task, flowContext.getParams());
	}

	public static class Agent{
		private String url;
		
		public Agent(String agentUrl) {
		}

		public void exec(FlowTask task, Map params) {
			
		}
		
	}
}