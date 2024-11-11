.PHONY: clean build npm-deps test rebuild
clean:
	rm -rf dist/
	rm -rf .shadow-cljs/
	rm -rf node_modules/
	rm package-lock.json
build:
	npx shadow-cljs release release
build-dbg:
	npx shadow-cljs release debug
npm-deps:
	npm install
test:
	rm -rf results.json && k6 run --verbose --out json=results.json dist/main.js
retest: build test
rebuild: clean deps build
