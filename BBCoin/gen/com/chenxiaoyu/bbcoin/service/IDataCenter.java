/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Coding\\Android\\BBCoin\\BBCoin\\src\\com\\chenxiaoyu\\bbcoin\\service\\IDataCenter.aidl
 */
package com.chenxiaoyu.bbcoin.service;
public interface IDataCenter extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.chenxiaoyu.bbcoin.service.IDataCenter
{
private static final java.lang.String DESCRIPTOR = "com.chenxiaoyu.bbcoin.service.IDataCenter";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.chenxiaoyu.bbcoin.service.IDataCenter interface,
 * generating a proxy if needed.
 */
public static com.chenxiaoyu.bbcoin.service.IDataCenter asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.chenxiaoyu.bbcoin.service.IDataCenter))) {
return ((com.chenxiaoyu.bbcoin.service.IDataCenter)iin);
}
return new com.chenxiaoyu.bbcoin.service.IDataCenter.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.chenxiaoyu.bbcoin.service.IDataCenter
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
}
}
}