const ValidateUtil = function () {
    this.isBlank = /^(|\s+)$/;
    this.isStartingWithBlank = /^\s+/;
}

ValidateUtil.prototype = {

    isEmptyString : function (str, msg) {

        if (this.isBlank.test(str)) {
            Common.alert(msg, 'red');
            return true;
        }

        if (this.isStartingWithBlank.test(str)) {
            Common.alert("첫 글자는 공란일 수 없습니다.", 'red');
            return true;
        }



        return false;
    }

    /*,

    validateTitle : function (str) {

        if (this.isBlank.test(str)) {
            Common.alert("제목을 입력해주세요", 'red');
            return false;
        }

        if (this.isStartingWithBlank.test(str)) {
            Common.alert("첫 글자는 공란일 수 없습니다.", 'red');
            return false;
        }



        return true;
    }*/
}