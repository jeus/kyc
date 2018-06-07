package com.b2mark.kyc;

import com.b2mark.kyc.enums.ImageType;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class SelfTestNotPush {

    @Test
    public void fromStr()
    {
        ImageType imageType = ImageType.fromString("cover");
        assertEquals(imageType ,ImageType.cover);
    }

    @Test
    public void fromStr1()
    {
        ImageType imageType = ImageType.fromString("passport");
        assertEquals(imageType ,ImageType.passport);
    }

    @Test
    public void getPath()
    {
        String rootPathStr = "/Users/jeus/Project/kyc/service/kyc/homeroot";
        Path homeroot = Paths.get(rootPathStr);
        ImageType imageType = ImageType.cover;
        assertEquals(rootPathStr +"/cover",imageType.getPath(homeroot).toAbsolutePath().toString());
    }
}
