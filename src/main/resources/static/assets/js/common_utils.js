/**
 * console 객체의 wrapper
 * - 객체 출력시 복제본(deep copy)을 사용함 (로깅 시점의 데이터 확인 가능)
 * - 로그 레벨 disabled, error, warn, info, debug 지원
 * @param level 로그레벨
 * @constructor
 */


// let Logger = function(level) {
//     this.setLevel(level || 'info');
// };

//Logger.prototype = {
const Logger = {
    _level: 'info',
    _nLevel: 3,     
    _levels: ['disabled', 'error', 'warn', 'info', 'debug'],
    setLevel: function(level) {
        if (typeof(console) != 'object') {
            this._level = 'disabled';
            this._nLevel = 0;
        }
        else {
            this._level = level;
            this._nLevel = this._levels.indexOf(this._level);
        }
    },
    getLevel: function() {
        return this._level;
    },
    enabled: function(level) {
        return (this._levels.indexOf(level) <= this._nLevel);
    },
    call: function(func, msg, args) {
        let a = [];
        if (typeof(msg) == 'string') {
            for (let i = 0; i < args.length; i++) {
                switch (typeof(args[i])) {
                    case 'undefined': msg += ', (undefined)'; break;
                    case 'string': msg += ', (string) %s'; a.push(args[i]); break;
                    case 'boolean': msg += ', (boolean) %o'; a.push(args[i]); break;
                    case 'number': msg += ', (number) %f'; a.push(args[i]); break;
                    case 'bigint': msg += ', (bigint) ' + args[i].toString(); break;
                    case 'function': msg += ', (function) %o'; a.push(args[i]); break;
                    case 'symbol': msg += ', (symbol) Symbol(' + Symbol.keyFor(args[i]) + ')'; break;
                    default:
                        if (args[i] == null) {
                            msg += ', (null)';
                        }
                        else if (typeof(args[i]) && args[i].nodeType == 1 && args[i].nodeName) {
                            msg += ', (element) %o';
                            a.push(JSON.parse(JSON.stringify({
                                tagName: args[i].tagName,
                                id: args[i].id,
                                className: args[i].className,
                                dataset: args[i].dataset,
                                nodeName: args[i].nodeName,
                                nodeType: args[i].nodeType,
                            })));
                        }
                        else {
                            msg += ', (object) %o';
                            try {
                                a.push(JSON.parse(JSON.stringify(args[i] || null)));
                            }
                            catch (e) { a.push(args[i] || null); }
                        }
                }
            }
        }
        else {
            for (let i = 0; i < args.length; i++)
                a.push(args[i]);
        }

        console.log(a);

        a.unshift(msg);
        func.apply(console, a);
    },
    log: function(msg, ...args) {
        if (this._nLevel > 0)
            this.call(console.log, msg, args);
    },
    error: function(msg, ...args) {
        if (this.enabled('error'))
            this.call(console.error || console.log, msg, args);
    },
    warn: function(msg, ...args) {
        if (this.enabled('warn'))
            this.call(console.warn || console.log, msg, args);
    },
    info: function(msg, ...args) {
        if (this.enabled('info'))
            this.call(console.info || console.log, msg, args);
    },
    debug: function(msg, ...args) {
        if (this.enabled('debug'))
            this.call(console.debug || console.log, msg, args);
    }
};

const Sequence = function Sequence(seed) {
    this.seed = seed || 0;
};
Sequence.prototype = {
    set: function set(val) {
        this.seed = val;
    },
    next: function next() {
        return ++this.seed;
    },
    current: function current() {
        return this.seed;
    }
};

/*
 * required jquery
 */
const API = function(url, options) {
    this.url = url;
    this.options = options || {};
};

API.prototype = {
    option: function(name, options, defaultValue) {
        options = options || {};
        return (options[name] !== undefined)?
            options[name]:
            (this.options[name] !== undefined)?
                this.options[name]:
                defaultValue;
    },
    headers: function(options) {
        let headers = {};
        if (this.options && this.options.headers) {
            for (let key in this.options.headers)
                headers[key] = this.options.headers[key];
        }
        if (options && options.headers) {
            for (let key in options.headers)
                headers[key] = options.headers[key];
        }
        this.credentialHeaders(headers, options);
        return headers;
    },
    hasCredential: function(options) {
        // TODO 인증 관련 설정
        return false;
    },
    credentialUsername: function(options) {
        // TODO 인증 관련 설정
        return null;
    },
    credentialPassword: function(options) {
        // TODO 인증 관련 설정
        return null;
    },
    credentialHeaders: function(headers, options) {
        // TODO 인증 관련 설정
    },
    handleError: function(jqXHR, textStatus, errorThrown, request) {
        let handler = this.option('error');
        let data = jqXHR.responseJSON || {};
        if (jqXHR.status == 400 && jqXHR.responseJSON) {
            errorThrown = jqXHR.responseJSON.rtMsg;
        }

        this.lastError = {data: data, jqXHR: jqXHR, textStatus: textStatus, errorThrown: errorThrown};

        Logger.error(errorThrown, data, jqXHR, textStatus);

        if (handler && typeof(handler) == 'function')
            handler(jqXHR, textStatus, errorThrown, request);
        else
            throw new Error(errorThrown);
    },
    callApi: function(method, path, data, options) {
        let url = this.url + ((path)? path: '');
        console.log("# callApi : url = " + url + ", method = " + method, data);

        let settings = {
            method: method,
            headers: this.headers(options),
            password: this.credentialPassword(options),
            username: this.credentialUsername(options),
            data: data,
            accepts: this.option('accepts', options),
            dataType: this.option('dataType', options),
            contentType: this.option('contentType', options),
            enctype: this.option('enctype', options),
            processData: this.option('processData', options),
            cache: this.option('cache', options),
            async: this.option('async', options),
            crossDomain: this.option('crossDomain', options),
            context: this,
            xhrFields: {
                withCredentials: this.hasCredential(options),
            }
        };
        // Logger.debug('call', url, settings);
        let req = {
            url: url,
            method: method,
            contentType: settings.contentType,
            data: data,
            stack: new Error()
        };

        /*
        jQuery.ajax(url, settings).then((data, textStatus, jqXHR) => {
                if (data.code == '0000') {
                    Logger.debug('success', data, textStatus, jqXHR);
                    return data;
                } else {
                    this.handleError(jqXHR, textStatus, data.message, req);
                }
            },
            (jqXHR, textStatus, errorThrown) => {
                this.handleError(jqXHR, textStatus, errorThrown, req);
            });

         */
        return new Promise((resolve, reject) => {
            jQuery.ajax(url, settings).then((data, textStatus, jqXHR) => {
                if (jqXHR.status === 200) {
                    //Logger.debug('success', data, textStatus, jqXHR);
                    resolve(data);
                } else {
                    this.handleError(jqXHR, textStatus, data.message, req);
                }
            },
            (jqXHR, textStatus, errorThrown) => {
                this.handleError(jqXHR, textStatus, errorThrown, req);
            });
        });
    },
    get: function(path, data, options) {
        return this.callApi('GET', path, data, options);
    },
    post: function(path, data, options) {
        return this.callApi('POST', path, data, options);
    },
    delete: function(path, data, options) {
        return this.callApi('DELETE', path, data, options);
    },
    put: function(path, data, options) {
        return this.callApi('PUT', path, data, options);
    }
};

const ErrorHandler = function ErrorHandler() {
    this.errors = [];
    this.api = new API('/api/v1/log', {debug: false});

    window.addEventListener('error', this.listener.bind(this));
    window.addEventListener('unhandledrejection', this.listener.bind(this));
};
ErrorHandler.prototype = {
    getStackTrace: function(e) {
        return (e.captureStackTrace && typeof e.captureStackTrace === 'function')?
            e.captureStackTrace(): e.stack;
    },
    listener: function onerror(event) {
        let err = {
            logTime: new Date().toISOString(),
            logLevel: 'ERROR'
        };
        if (event.type == 'unhandledrejection') {
            err.thread = 'promise';
            let m = event.reason.stack.match(/\n *at (.*?):\d+:\d+(\n|$)/);
            err.logger = (m)? m[1]: null;
            err.message = event.reason.message;
            err.trace = this.getStackTrace(event.reason);
        }
        else {
            err.thread = 'window';
            err.logger = event.filename;
            err.message = event.error.message;
            err.trace = this.getStackTrace(event.error);
        }
        // this.errors.push(err);
        this.save(err);
    },
    handleError: function handleError(jqXHR, textStatus, errorThrown, request) {
        let trace = this.getStackTrace(request.stack);
        delete request.stack;
        let err = {
            logTime: new Date().toISOString(),
            logLevel: 'ERROR',
            thread: 'ajax',
            logger: request.url,
            message: errorThrown + "\nRequest: " + JSON.stringify(request) + "\nResponse: " + JSON.stringify(jqXHR),
            trace: trace
        };
        // this.errors.push(err);
        this.save(err);
    },
    save: function save(err) {
        this.api.post('', JSON.stringify(err), {contentType: "application/json"})
            .then((result) => {})
            .catch((reason) => {})
        ;
    }
};

let StringUtils = {
    // 자료형과 상관없이 빈값 여부 리턴
    isEmpty: function(value) {
        return ( value == null
            || value == undefined
            || value === ""
            || ( value != null && typeof value == "object" && !Object.keys(value).length )
        );
    },

    isNotEmpty: function(value) {
        return !this.isEmpty(value);
    },

    isDateFormat: function(value) {
        let datetimeRegexp = /[0-9]{4}-[0-9]{2}-[0-9]{2}/;
        return datetimeRegexp.test(value);
    },

    isEmailFormat: function(value) {
        const reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
        return reg_email.test(value);
    }
};

const ArrayUtils = {
    _cb: function _cb(c, t, d) {
        return (c && typeof c == 'function')? c.bind(t): d;
    },
    _cmp: function _cmp(c, t) {
        return ArrayUtils._cb(c, t, function compare(a, b) { return (a > b)? 1: ((a < b)? -1: 0); });
    },
    _eq: function _eq(c, t) {
        return ArrayUtils._cb(c, t, function equals(a, b) { return a === b; });
    },
    _it: function Iterable(a, c, t) {
        this.a = a;
        this.c = ArrayUtils._cb(c, t, function callback(r, i, arr) { return r; });
        this[Symbol.iterator] = function iterator() {
            let t = this;
            let i = 0;
            return {
                next: function () {
                    if (i < t.a.length)
                        return {value: t.c(t.a[i], i++, t.a), done: false};
                    else
                        return {done: true};
                }
            };
        };
    },
    addArrayMethods: function() {
        let methods = [];
        if (arguments.length == 0)
            methods = ['last', 'first', 'previous', 'next', 'max', 'min', 'sum', 'reverseFind', 'reverseFindIndex'];
        else {
            for (let i = 0; i < arguments.length; i++) {
                if (Array.isArray(arguments[i]))
                    methods = methods.concat(arguments[i]);
                else
                    methods.push(arguments[i]);
            }
        }

        for (const method of methods) {
            if (ArrayUtils[method] && typeof ArrayUtils[method] == 'function' &&
                !Array.prototype[method]) {
                Array.prototype[method] = (function(method) {
                    return function() {
                        let args = [this];
                        for (let i = 0; i < arguments.length; i++) {
                            args.push(arguments[i]);
                        }
                        return ArrayUtils[method].apply(this, args);
                    };
                })(method);
            }
        }
    },
    map: function map(array, callback, thisArg) {
        callback = ArrayUtils._cb(callback, thisArg, function callback(r, i, arr) { return r; });
        if (Array.isArray(array))
            return array.map(callback);
        else {
            let mapped = [];
            for (let i = 0; i < array.length; i++)
                mapped[i] = callback(array[i], i, array);
            return mapped;
        }
    },
    forEach: function forEach(array, callback, thisArg) {
        callback = ArrayUtils._cb(callback, thisArg, function callback(r, i, arr) {});
        if (Array.isArray(array))
            array.forEach(callback);
        else {
            for (let i = 0; i < array.length; i++) {
                callback(array[i], i, array);
            }
        }
    },
    find: function find(array, callback, thisArg) {
        if (Array.isArray(array))
            return array.find(callback, thisArg);
        else {
            let index = ArrayUtils.findIndex(array, callback, thisArg);
            return (index == -1)? undefined: array[index];
        }
    },
    findIndex: function findIndex(array, callback, thisArg) {
        if (Array.isArray(array))
            return array.findIndex(callback, thisArg);
        else {
            for (let i = 0; i < array.length; i++) {
                if (callback.apply(thisArg, array[i], i, array))
                    return i;
            }
            return -1;
        }
    },
    reverseFind: function reverseFind(array, callback, thisArg) {
        let index = ArrayUtils.reverseFindIndex(array, callback, thisArg);
        return (index == -1)? undefined: array[index];
    },
    reverseFindIndex: function reverseFindIndex(array, callback, thisArg) {
        for (let i = array.length - 1; i >= 0; i--) {
            if (callback(array[i], i, array))
                return i;
        }
        return -1;
    },
    values: function values(array) {
        return (Array.isArray(array))? array.values(): new ArrayUtils.Iterable(array);
    },
    keys: function entries(array) {
        return (Array.isArray(array))? array.keys(): new ArrayUtils.Iterable(array, (r, i) => i);
    },
    entries: function entries(array) {
        return (Array.isArray(array))? array.entries(): new ArrayUtils.Iterable(array, (r, i) => [i, r]);
    },
    at: function at(array, index) {
        if (Array.isArray(array))
            return array.at(index);
        else {
            index = Math.trunc(index) || 0;
            if (index < 0)
                index += array.length;
            if (index < 0 || index >= array.length)
                return undefined;
            return array[index];
        }
    },
    reduce: function reduce(array, callback, initialValue) {
        if (Array.isArray(array))
            return array.reduce(callback, initialValue);
        else {
            let acc = initialValue || array[0];
            for (let i = 0; i < array.length; i++)
                acc = reduce(acc, array[i], i, array);
            return acc;
        }
    },
    first: function first(array) {
        return array[0];
    },
    last: function last(array) {
        return ArrayUtils.at(array, -1);
    },
    previous: function(array, element, equals, thisArg) {
        equals = ArrayUtils._eq(equals, thisArg);
        if (array.length === 0 || equals(ArrayUtils.first(array), element))
            return undefined;
        for (let i = 1; i < array.length; i++) {
            if (equals(array[i], element))
                return array[i - 1];
        }
        return undefined;
    },
    next: function(array, element, equals, thisArg) {
        equals = ArrayUtils._eq(equals, thisArg);
        if (array.length === 0 || equals(ArrayUtils.last(array), element))
            return undefined;
        for (let i = 0; i < array.length - 1; i++) {
            if (equals(array[i], element))
                return array[i + 1];
        }
        return undefined;
    },
    max: function max(array, compare, thisArg) {
        compare = ArrayUtils._cmp(compare, thisArg);
        let c;
        for (let i = 0; i < array.length; i++) {
            let r = array[i];
            if (!c)
                c = r;
            else if (compare(c, r) < 0)
                c = r;
        }
        return c;
    },
    min: function max(array, compare, thisArg) {
        compare = ArrayUtils._cmp(compare, thisArg);
        let c;
        for (let i = 0; i < array.length; i++) {
            let r = array[i];
            if (!c)
                c = r;
            else if (compare(c, r) > 0)
                c = r;
        }
        return c;
    },
    sum: function sum(array, callback, thisArg) {
        let sum = 0;
        ArrayUtils.map(array, callback, thisArg).forEach(r => {
            sum += Number(r);
        });
        return sum;
    },
    join: function join(array, delimiter, callback, thisArg) {
        let str = '';
        ArrayUtils.map(array, callback, thisArg).forEach((r, i) => {
            str += ((i > 0)? delimiter: '') + String(r);
        });
        return str;
    }
};

const UrlUtils = {
    parseQuery: function (queryString) {
        let query = {};
        let pairs = (queryString[0] === '?' ? queryString.substr(1) : queryString).split('&');
        for (var i = 0; i < pairs.length; i++) {
            var pair = pairs[i].split('=');
            query[decodeURIComponent(pair[0])] = decodeURIComponent(pair[1] || '');
        }
        return query;
    },
};