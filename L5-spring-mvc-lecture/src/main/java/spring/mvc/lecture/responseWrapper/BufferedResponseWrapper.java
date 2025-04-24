package spring.mvc.lecture.responseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class BufferedResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream capture;
    private final ServletOutputStream output;
    private final PrintWriter writer;

    public BufferedResponseWrapper(HttpServletResponse response) {
        super(response);
        capture = new ByteArrayOutputStream();
        output = new ServletOutputStream() {
            @Override
            public void write(int b) {
                capture.write(b);
            }

            @Override
            public boolean isReady() { return true; }

            @Override
            public void setWriteListener(WriteListener listener) {}
        };
        writer = new PrintWriter(new OutputStreamWriter(capture, StandardCharsets.UTF_8));
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return output;
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    public byte[] getCapturedAsBytes() {
        writer.flush();
        return capture.toByteArray();
    }

    public String getCapturedAsString() {
        return new String(getCapturedAsBytes(), StandardCharsets.UTF_8);
    }
}
