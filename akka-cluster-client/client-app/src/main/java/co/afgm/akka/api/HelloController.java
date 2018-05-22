package co.afgm.akka.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@Autowired
	ServiceImpl service;

	@RequestMapping("hello")
	public void queryStatus() {

		service.sayHello();

	}

}
