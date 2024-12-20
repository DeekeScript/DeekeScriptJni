package top.deeke.jni.v8.inspector;

public interface DebuggerConnectionListener {
    public void onDebuggerConnected();

    public void onDebuggerDisconnected();
}