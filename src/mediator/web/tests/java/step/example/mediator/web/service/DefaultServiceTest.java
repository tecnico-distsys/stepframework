package step.example.mediator.web.service;

public class DefaultServiceTest extends step.framework.service.ServiceTest {
	@Override
	protected String getSetupDataSetName() {
		return "/mediator.xml";
	}
}