const ConvertUtil = {
    /**
     * queryString을 json 으로 변환
     */
    parseQuery: function (queryString) {
        let query = {};
        let pairs = (queryString[0] === '?' ? queryString.substr(1) : queryString).split('&');
        for (var i = 0; i < pairs.length; i++) {
            var pair = pairs[i].split('=');
            query[decodeURIComponent(pair[0])] = decodeURIComponent(pair[1] || '');
        }
        return query;
    },

    /**
     * json을 queryString으로 변환
     */
    jsonToQueryString(json) {
        return Object.entries(json)
            .map( ([key,value]) => ( value && key+'='+value ))
            .filter(v=>v).join('&');
    }

}