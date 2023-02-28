package myself.badwritten.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * 上传文件实体
 * 
 * @author Hugo
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Upload {

	private File file;
	private String fileName;
	private Long fileSize;
	private String contentType;

	/**
	 * 是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return file == null;
	}

	/**
	 * 是否不为空
	 * 
	 * @return
	 */
	public boolean isNotEmpty() {
		return !isEmpty();
	}

}
