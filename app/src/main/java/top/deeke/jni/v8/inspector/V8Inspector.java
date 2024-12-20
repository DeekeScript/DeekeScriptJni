package top.deeke.jni.v8.inspector;

import java.util.ArrayList;
import java.util.List;

import top.deeke.jni.v8.V8;
import top.deeke.jni.v8.V8Object;

public class V8Inspector {

    private final V8 runtime;
    private long inspectorPtr = 0;
    private boolean waitingForConnection = true;
    private final List<DebuggerConnectionListener> debuggerConnectionListeners;

    protected V8Inspector(final V8 runtime, final V8InspectorDelegate inspectorDelegate, final String contextName) {
        this.runtime = runtime;
        inspectorPtr = runtime.createInspector(inspectorDelegate, contextName);
        debuggerConnectionListeners = new ArrayList<DebuggerConnectionListener>();
    }

    public static V8Inspector createV8Inspector(final V8 runtime, final V8InspectorDelegate inspectorDelegate, final String contextName) {
        return new V8Inspector(runtime, inspectorDelegate, contextName);
    }

    public static V8Inspector createV8Inspector(final V8 runtime, final V8InspectorDelegate inspectorDelegate) {
        return new V8Inspector(runtime, inspectorDelegate, null);
    }

    public void dispatchProtocolMessage(final String protocolMessage) {
        try {
            runtime.dispatchProtocolMessage(inspectorPtr, protocolMessage);
            if (waitingForConnection) {
                verifyDebuggerConnection(protocolMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDebuggerConnectionListener(final DebuggerConnectionListener listener) {
        debuggerConnectionListeners.add(listener);
    }

    public void removeDebuggerConnectionListener(final DebuggerConnectionListener listener) {
        debuggerConnectionListeners.remove(listener);
    }

    private void verifyDebuggerConnection(final String protocolMessage) {
        V8Object json = null;
        try {
            json = runtime.executeObjectScript("JSON.parse(JSON.stringify(" + protocolMessage + "))");
            if (json.getString("method").equals("Runtime.runIfWaitingForDebugger")) {
                waitingForConnection = false;
                runtime.schedulePauseOnNextStatement(inspectorPtr, "");
                for (DebuggerConnectionListener listener : debuggerConnectionListeners) {
                    listener.onDebuggerConnected();
                }
            }
        } finally {
            if (json != null) {
                json.close();
            }
        }
    }

}