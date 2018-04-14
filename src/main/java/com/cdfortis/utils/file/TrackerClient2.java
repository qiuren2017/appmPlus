package com.cdfortis.utils.file;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import com.cdfortis.utils.config.SystemConfig;

public class TrackerClient2 extends TrackerClient{
	/**
	* query storage server to upload file
	* @param trackerServer the tracker server
	* @param groupName the group name to upload file to, can be empty
	* @return storage server object, return null if fail
	*/
	@Override
	public StorageServer getStoreStorage(TrackerServer trackerServer, String groupName) throws IOException
	{
		byte[] header;
		String ip_addr;
		int port;
		byte cmd;
		int out_len;
		boolean bNewConnection;
		byte store_path;
		Socket trackerSocket;
		
		if (trackerServer == null)
		{
			trackerServer = getConnection();
			if (trackerServer == null)
			{
				return null;
			}
			bNewConnection = true;
		}
		else
		{
			bNewConnection = false;
		}

		trackerSocket = trackerServer.getSocket();
		OutputStream out = trackerSocket.getOutputStream();
		
		try
		{
			if (groupName == null || groupName.length() == 0)
			{
				String net_method = "0";
				net_method = SystemConfig.getFDFSConfigResource("net_method").toString().trim();
				cmd = ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_IN_OUT_IP_CHANGE;
				if("0".equals(net_method)){
					cmd = ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
				}
				out_len = 0;
			}
			else
			{
				cmd = ProtoCommon.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE;
				out_len = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN;
			}
			header = ProtoCommon.packHeader(cmd, out_len, (byte)0);
			out.write(header);

			if (groupName != null && groupName.length() > 0)
			{
				byte[] bGroupName;
				byte[] bs;
				int group_len;
				
				bs = groupName.getBytes(ClientGlobal.g_charset);
				bGroupName = new byte[ProtoCommon.FDFS_GROUP_NAME_MAX_LEN];
				
				if (bs.length <= ProtoCommon.FDFS_GROUP_NAME_MAX_LEN)
				{
					group_len = bs.length;
				}
				else
				{
					group_len = ProtoCommon.FDFS_GROUP_NAME_MAX_LEN;
				}
				Arrays.fill(bGroupName, (byte)0);
				System.arraycopy(bs, 0, bGroupName, 0, group_len);
				out.write(bGroupName);
			}
	
			ProtoCommon.RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(trackerSocket.getInputStream(), 
	                                     ProtoCommon.TRACKER_PROTO_CMD_RESP, 
	                                     ProtoCommon.TRACKER_QUERY_STORAGE_STORE_BODY_LEN);
			this.errno = pkgInfo.errno;
			if (pkgInfo.errno != 0)
			{
				return null;
			}
			
			ip_addr = new String(pkgInfo.body, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN, ProtoCommon.FDFS_IPADDR_SIZE-1).trim();
	
			port = (int)ProtoCommon.buff2long(pkgInfo.body, ProtoCommon.FDFS_GROUP_NAME_MAX_LEN
	                        + ProtoCommon.FDFS_IPADDR_SIZE-1);
			store_path = pkgInfo.body[ProtoCommon.TRACKER_QUERY_STORAGE_STORE_BODY_LEN - 1];
			
			return new StorageServer(ip_addr, port, store_path);
		}
		catch(IOException ex)
		{
			if (!bNewConnection)
			{
				try
				{
					trackerServer.close();
				}
				catch(IOException ex1)
				{
					ex1.printStackTrace();
				}
			}
			
			throw ex;
		}
		finally
		{
			if (bNewConnection)
			{
				try
				{
					trackerServer.close();
				}
				catch(IOException ex1)
				{
					ex1.printStackTrace();
				}
			}
		}
	}
}
