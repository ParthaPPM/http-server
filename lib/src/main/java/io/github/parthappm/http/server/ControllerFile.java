package io.github.parthappm.http.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

class ControllerFile implements Controller
{
	private final String ROOT_DIRECTORY;
	private final int MAX_FILE_SIZE;

	ControllerFile(ServerProperties properties)
	{
		ROOT_DIRECTORY = getAbsolutePath(properties.getRootDirectory());
		MAX_FILE_SIZE = 10485760; // 10MB
	}

	private String getAbsolutePath(String path)
	{
		try
		{
			return new File(path).getCanonicalPath();
		}
		catch (IOException | NullPointerException ignored) {}
		return null;
	}

	private String getMimeType(String fileName)
	{
		int dotIndex = fileName.lastIndexOf('.');
		String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex+1).toLowerCase();
		return switch (extension) {
			case "aac" -> "audio/aac";
			case "abw" -> "application/x-abiword";
			case "arc" -> "application/x-freearc";
			case "avif" -> "image/avif";
			case "avi" -> "video/x-msvideo";
			case "azw" -> "application/vnd.amazon.ebook";
			case "bmp" -> "image/bmp";
			case "bz" -> "application/x-bzip";
			case "bz2" -> "application/x-bzip2";
			case "cda" -> "application/x-cdf";
			case "csh" -> "application/x-csh";
			case "css" -> "text/css";
			case "csv" -> "text/csv";
			case "doc" -> "application/msword";
			case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			case "eot" -> "application/vnd.ms-fontobject";
			case "epub" -> "application/epub+zip";
			case "gz" -> "application/gzip";
			case "gif" -> "image/gif";
			case "htm", "html" -> "text/html";
			case "ico" -> "image/vnd.microsoft.icon";
			case "ics" -> "text/calendar";
			case "jar" -> "application/java-archive";
			case "jpeg", "jpg" -> "image/jpeg";
			case "js", "mjs" -> "text/javascript";
			case "json" -> "application/json";
			case "jsonld" -> "application/ld+json";
			case "mid", "midi" -> "audio/midi";
			case "mkv" -> "video/x-matroska";
			case "mp3" -> "audio/mpeg";
			case "mp4" -> "video/mp4";
			case "mpeg" -> "video/mpeg";
			case "mpkg" -> "application/vnd.apple.installer+xml";
			case "odp" -> "application/vnd.oasis.opendocument.presentation";
			case "ods" -> "application/vnd.oasis.opendocument.spreadsheet";
			case "odt" -> "application/vnd.oasis.opendocument.text";
			case "oga" -> "audio/ogg";
			case "ogv" -> "video/ogg";
			case "ogx" -> "application/ogg";
			case "opus" -> "audio/opus";
			case "otf" -> "font/otf";
			case "png" -> "image/png";
			case "pdf" -> "application/pdf";
			case "php" -> "application/x-httpd-php";
			case "ppt" -> "application/vnd.ms-powerpoint";
			case "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation";
			case "rar" -> "application/vnd.rar";
			case "rtf" -> "application/rtf";
			case "sh" -> "application/x-sh";
			case "svg" -> "image/svg+xml";
			case "tar" -> "application/x-tar";
			case "tif", "tiff" -> "image/tiff";
			case "ts" -> "video/mp2t";
			case "ttf" -> "font/ttf";
			case "txt" -> "text/plain";
			case "vsd" -> "application/vnd.visio";
			case "wav" -> "audio/wav";
			case "weba" -> "audio/webm";
			case "webm" -> "video/webm";
			case "webp" -> "image/webp";
			case "woff" -> "font/woff";
			case "woff2" -> "font/woff2";
			case "xhtml" -> "application/xhtml+xml";
			case "xls" -> "application/vnd.ms-excel";
			case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			case "xml" -> "application/xml";
			case "xul" -> "application/vnd.mozilla.xul+xml";
			case "zip" -> "application/zip";
			case "3gp" -> "video/3gpp";
			case "3g2" -> "video/3gpp2";
			case "7z" -> "application/x-7z-compressed";
			default -> "application/octet-stream"; // "bin" is also considered as "application/octet-stream"
		};
	}

	private byte[] readFromFile(String fileName)
	{
		if (ROOT_DIRECTORY != null)
		{
			String absoluteFileName = getAbsolutePath(ROOT_DIRECTORY + "/" + URLDecoder.decode(fileName, StandardCharsets.UTF_8));
			if (absoluteFileName != null && absoluteFileName.startsWith(ROOT_DIRECTORY))
			{
				try (InputStream is = new FileInputStream(absoluteFileName))
				{
					return is.readNBytes(MAX_FILE_SIZE);
				}
				catch (IOException | NullPointerException | OutOfMemoryError ignored) {}
			}
		}
		return null;
	}

	public Response process(Request request)
	{
		Response response;
		String path = request.getPath();
		if (path.equals("/"))
		{
			response = new Response(200);
		}
		else
		{
			byte[] responseBody = readFromFile(path);
			if (responseBody != null)
			{
				response = new Response().addHeader("Content-Type", getMimeType(path)).setBody(responseBody);
			}
			else
			{
				response = new Response(404);
			}
		}
		return response;
	}
}
