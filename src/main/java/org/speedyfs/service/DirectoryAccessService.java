package org.speedyfs.service;

import java.util.List;

import org.speedyfs.dto.ResponseDTO;
import org.speedyfs.enums.AccessType;

public interface DirectoryAccessService {
	ResponseDTO changeDirectoryAccess(Integer directoryId, AccessType accessType);

	ResponseDTO grantDirectoryAccess(Integer directoryId, List<String> userNames);

	ResponseDTO checkDirectoryAccess(Integer directoryId);

	ResponseDTO getDirectoryUsers(Integer directoryId);

	ResponseDTO revokeUserDirectoryAccess(Integer directoryId, List<String> userNames);
}
