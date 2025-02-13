package gov.nist.hit.hl7.codesetauthoringtool.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class ForwardingController {

    @RequestMapping(value = {"/{path:(?!resources$)[^.]*}/**", "/"})
    public String redirect() {
        return "forward:/resources/index.html";
    }
}