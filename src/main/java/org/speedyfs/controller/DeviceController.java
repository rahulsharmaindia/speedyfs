package org.speedyfs.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.speedyfs.dto.DeviceListDTO;
import org.speedyfs.dto.ServerListDTO;
import org.speedyfs.exception.DeviceDataException;
import org.speedyfs.model.Device;
import org.speedyfs.model.OtherServer;
import org.speedyfs.service.DeviceService;

/**
 * <p>
 * Controller for device accessing the web service
 * <p>
 * Add Device
 * <p>
 * Update Device
 * <p>
 * List all Devices
 * <p>
 * List all Servers
 * @author rahul.sharma3
 *
 */
@RestController
@RequestMapping(value = "/devices")
public class DeviceController extends BaseController {
	@Autowired
	DeviceService service;
	@Value("#{'${servers}'.split(',')}")
	private List<String> servers;
	@Value("#{'${server.path}'.split(',')}")
	private List<String> serverPaths;
	
	@Value("#{'${webserver}'}")
	private String webserver;
	@Value("#{'${webserver.path}'}")
	private String webserverPath;

	/**
	 * To add a new device or update an old device consuming the services.
	 * 
	 * @param device
	 *            Device object
	 * @param request
	 *            HttpServletRequest
	 * @return ResponseEntity&lt;String&gt;
	 */
	@RequestMapping(value = "/add-update-device", method = RequestMethod.POST)
	public ResponseEntity<String> addUpdateDevice(@RequestBody Device device, HttpServletRequest request) {
		device.setPublicIP(!("").equals(device.getPublicIP()) ? device.getPublicIP() : request.getRemoteAddr());
			service.updateDevice(device);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * To fetch the list of devices registered with the same public IP.
	 * 
	 * @param IPAddress
	 *            public IP to search nearby IPs
	 * @param request
	 *            HttpServletRequest
	 * @return ResponseEntity&lt;{@link DeviceListDTO}&gt;
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/list-devices", method = RequestMethod.POST)
	public ResponseEntity<DeviceListDTO> listDevices(@RequestParam("IPAddress") String IPAddress, HttpServletRequest request) {
		List devices;
		DeviceListDTO deviceListDTO = new DeviceListDTO();
			devices = service.fetchDevice(!("").equals(IPAddress) ? IPAddress : request.getRemoteAddr());
			deviceListDTO.setDevices(devices);
		if (devices.size() > 0) {
			return new ResponseEntity<DeviceListDTO>(deviceListDTO, HttpStatus.OK);
		} else {
			throw new DeviceDataException();
		}
	}

	/**
	 * To fetch the list of servers configured in properties file.
	 * 
	 * @return ResponseEntity&lt;{@link ServerListDTO}&gt;
	 */
	@RequestMapping(value = "/list-servers", method = RequestMethod.GET)
	public ResponseEntity<ServerListDTO> listServers() {
		List<OtherServer> otherServers =new ArrayList<>();
		ServerListDTO dto = new ServerListDTO();
		int count=0;
		for(String s : servers){
			OtherServer server = new OtherServer();
			server.setName(s);
			String path =serverPaths.get(count++);
			server.setPath(path==null?"":path);
			otherServers.add(server);
		}
		if(!webserver.isEmpty() && !webserverPath.isEmpty()) {
			OtherServer liferayServer = new OtherServer();
			liferayServer.setName(webserver);
			liferayServer.setPath(webserverPath);
			dto.setWebServer(liferayServer);
		}
		dto.setServers(otherServers);
		return otherServers.size() == 0 ? new ResponseEntity<ServerListDTO>(dto, HttpStatus.NO_CONTENT) : new ResponseEntity<ServerListDTO>(dto, HttpStatus.OK);
	}
}
