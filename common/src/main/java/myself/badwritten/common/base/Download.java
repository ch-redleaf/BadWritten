package myself.badwritten.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

/**
 * 下载文件实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Download {

	private InputStream inputStream;
	private String fileName;
	private Long fileSize;
	private String contentType = "application/octet-stream";
	private String contentDisposition = "attachment";
	private String charset = "utf-8";

}
