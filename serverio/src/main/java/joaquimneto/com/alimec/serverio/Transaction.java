package joaquimneto.com.alimec.serverio;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by KithLenovo on 23/02/2015.
 */
public class Transaction {

	//
	//

	private static ExecutorService executor = Executors.newSingleThreadExecutor();;
	private String serverAddr;
	private int serverPorta;

	private boolean exceptionOcourred = false;

	public Transaction(String address, int port) {
		this.serverAddr = address;
		this.serverPorta = port;

	}

	public static Transaction newTransaction(String address, int port) {
		return new Transaction(address, port);
	}

	public JSONObject fazerComando(final JSONObject comando) throws IOException {
		return fazerComando(comandoTCP(comando));
	}

	public JSONObject fazerComandoUDP(final JSONObject comando) throws IOException {
		return fazerComando(comandoUDP(comando));
	}

	private JSONObject fazerComando(Callable<Object> comando) throws IOException {

		Future<Object> result = executor.submit(comando);

		Object response = null;
		try {
			response = result.get();

			if (response == null) {
				return null;
			}

			if (exceptionOcourred) {
				throw (IOException) response;
			}
			JSONObject jsonResponse = new JSONObject((String)response);
			return jsonResponse;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			throw new IOException(e);
		}

		return null;
	}

	private boolean execOnWatchdog(long timeout, Runnable run) throws SocketTimeoutException {

		Thread execution = new Thread(run);

		execution.start();

		long startTime = System.currentTimeMillis();
		while (startTime + timeout > System.currentTimeMillis()) {
			try {
				if (!execution.isAlive()) {
					return true;
				} else {
					Thread.sleep(250);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		throw new SocketTimeoutException("Watchdog timer expired and operation wasn't finished");
	}

	private Callable<Object> comandoUDP(final JSONObject comando) {

		return new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				try {
					final DatagramSocket sock = new DatagramSocket();

					String message = comando.toString();

					DatagramPacket sendPack = new DatagramPacket(message.getBytes(), message.length(),
							InetAddress.getByName(serverAddr), serverPorta);
					sock.send(sendPack);

					final DatagramPacket recvPack = new DatagramPacket(new byte[512], 512);
					execOnWatchdog(4000, new Runnable() {
						@Override
						public void run() {
							try {
								sock.receive(recvPack);
							} catch (IOException e) {
								exceptionOcourred = true;
							}
						}
					});

					String result = new String(recvPack.getData());
					sock.close();
					return result;
				} catch (Exception e) {
					e.printStackTrace();
					exceptionOcourred = true;
					return e;
				}
			}

		};

	}

	private Callable<Object> comandoTCP(final JSONObject comando) {

		return new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				Socket comm = null;
				String message = comando.toString();
				try {
					comm = new Socket();
					comm.connect(new InetSocketAddress(serverAddr, serverPorta), 4000);

					BufferedOutputStream out = new BufferedOutputStream(comm.getOutputStream());
					BufferedReader in = new BufferedReader(new InputStreamReader(comm.getInputStream()));

					out.write(message.getBytes());
					out.write(255);
					out.flush();

					StringWriter response = new StringWriter();
					int readen = -1;
					while (true) {
						readen = in.read();

						if (readen < 0 || readen > 254) {
							break;
						}

						response.write((char) readen);
					}
					comm.close();
					return response.toString();
				} catch (Exception e) {
					exceptionOcourred = true;
					return e;
				}
			}
		};
	}

}
