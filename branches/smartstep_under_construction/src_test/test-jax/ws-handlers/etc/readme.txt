/msg-ctx
Sample contents of Message Contexts

/trace
Sample output from extension invocation traces

File names legend:

c - client handler
s - server handler

i - inbound
o - outbound

ok - returns true

rf - returns false
tr - throws RuntimeException
ts - throws SOAPFaultException

app - server application

e - throws declared Exception
se - throws declared Exception wrapping a run-time exception
rte - throws an undeclared run-time exception


Example:

co-ok-si-ts.txt
Client handler returns true, Service handler throws a SOAPFaultException when it receives an inbound message
