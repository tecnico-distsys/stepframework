package step.proto.flight.core.service;
public class DefaultServiceTest extends step.framework.service.ServiceTest {
    @Override
    protected String getSetupDataSetName() {
	return "/flight-core.xml";
    }
}