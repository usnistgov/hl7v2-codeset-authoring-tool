package gov.nist.hit.hl7.codesetauthoringtool.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class ForwardingController {

    @RequestMapping(value = "/{path:(?!static$)[^.]*}/**")
    public String redirect() {
        return "forward:/static/index.html";
    }
}