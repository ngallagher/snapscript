package org.snapscript.engine;

// 1) start engine container on ephemeral port
// 2) ensure stdout/stderr goes to both client and console
// 3) once server is running launch the agent with agent-pool=0
// 4) as soon as the agent is running, issue an ExecuteCommand ---- should be the same process11!
// 5) once connected the process should suspend
public class ProcessEngineScript {

}
