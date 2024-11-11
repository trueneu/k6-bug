# k6 v0.53 regression

Dependencies:
 - npm, npx
 - k6
 - leiningen (https://leiningen.org/)

Everything else should get installed from package.json + project.clj or as transitives (like OpenJDK).

## Makefile

Includes several targets to make reproduction a bit easier.

 - `make build` to compile
 - `make test` to run k6

and several others that should be self-explanatory.

`make build-dbg` is left for the sake of completeness: Google Closure complains about the debug loader and I didn't bother figuring out how to fix it. But maybe unoptimized code provides additional insights. 


## repro

1. Install k6 v0.52
```
$ k6 version
k6 v0.52.0 (commit/20f8febb5b, go1.22.4, linux/amd64)
```
2. Run `make build && make test`. The test passes, console output shows that `http` import has callables; requests are made, metrics registered:
```
INFO[0008] Running req-simple                            source=console
INFO[0008] http keys: TLS_1_0,TLS_1_1,TLS_1_2,TLS_1_3,OCSP_STATUS_GOOD,OCSP_STATUS_REVOKED,OCSP_STATUS_SERVER_FAILED,OCSP_STATUS_UNKNOWN,OCSP_REASON_UNSPECIFIED,OCSP_REASON_KEY_COMPROMISE,OCSP_REASON_CA_COMPROMISE,OCSP_REASON_AFFILIATION_CHANGED,OCSP_REASON_SUPERSEDED,OCSP_REASON_CESSATION_OF_OPERATION,OCSP_REASON_CERTIFICATE_HOLD,OCSP_REASON_REMOVE_FROM_CRL,OCSP_REASON_PRIVILEGE_WITHDRAWN,OCSP_REASON_AA_COMPROMISE,url,CookieJar,cookieJar,file,get,head,post,put,patch,del,options,request,asyncRequest,batch,setResponseCallback,expectedStatuses,default  source=console
INFO[0008] http/default keys: TLS_1_0,TLS_1_1,TLS_1_2,TLS_1_3,OCSP_STATUS_GOOD,OCSP_STATUS_REVOKED,OCSP_STATUS_SERVER_FAILED,OCSP_STATUS_UNKNOWN,OCSP_REASON_UNSPECIFIED,OCSP_REASON_KEY_COMPROMISE,OCSP_REASON_CA_COMPROMISE,OCSP_REASON_AFFILIATION_CHANGED,OCSP_REASON_SUPERSEDED,OCSP_REASON_CESSATION_OF_OPERATION,OCSP_REASON_CERTIFICATE_HOLD,OCSP_REASON_REMOVE_FROM_CRL,OCSP_REASON_PRIVILEGE_WITHDRAWN,OCSP_REASON_AA_COMPROMISE,url,CookieJar,cookieJar,file,get,head,post,put,patch,del,options,request,asyncRequest,batch,setResponseCallback,expectedStatuses  source=console
INFO[0008] Running req-default                           source=console
INFO[0008] http keys: TLS_1_0,TLS_1_1,TLS_1_2,TLS_1_3,OCSP_STATUS_GOOD,OCSP_STATUS_REVOKED,OCSP_STATUS_SERVER_FAILED,OCSP_STATUS_UNKNOWN,OCSP_REASON_UNSPECIFIED,OCSP_REASON_KEY_COMPROMISE,OCSP_REASON_CA_COMPROMISE,OCSP_REASON_AFFILIATION_CHANGED,OCSP_REASON_SUPERSEDED,OCSP_REASON_CESSATION_OF_OPERATION,OCSP_REASON_CERTIFICATE_HOLD,OCSP_REASON_REMOVE_FROM_CRL,OCSP_REASON_PRIVILEGE_WITHDRAWN,OCSP_REASON_AA_COMPROMISE,url,CookieJar,cookieJar,file,get,head,post,put,patch,del,options,request,asyncRequest,batch,setResponseCallback,expectedStatuses  source=console
<skipped>
     http_reqs......................: 2      7.88881/s

```

3. Install k6 v0.53
```
$ k6 version
k6 v0.53.0 (commit/f82a27da8f, go1.22.6, linux/amd64)
```
4. Run `make build && make test`. `http` is now "undefined or null":
```
INFO[0000] Running req-simple                            source=console
INFO[0000] http keys:                                    source=console
ERRO[0000] TypeError: Cannot convert undefined or null to object
        at keys (native)
        at Ce (file:///home/pgu/git_tree/k6-bug/dist/main.js:76:126(35))  executor=shared-iterations scenario=default source=stacktrace
```
5. `req-default` variant gives the same error:
```
INFO[0000] Running req-default                           source=console
ERRO[0000] TypeError: Cannot convert undefined or null to object
        at keys (native)
        at Ce (file:///home/pgu/git_tree/k6-bug/dist/main.js:76:43(16))  executor=shared-iterations scenario=default source=stacktrace
```
6. If you comment out `Object.keys` and leave just the `http/get` call, `http` stays undefined:
```
INFO[0000] Running req-default                           source=console
ERRO[0000] TypeError: Cannot read property 'get' of undefined or null
        at Ce (file:///home/pgu/git_tree/k6-bug/dist/main.js:76:1(7))  executor=shared-iterations scenario=default source=stacktrace

```
