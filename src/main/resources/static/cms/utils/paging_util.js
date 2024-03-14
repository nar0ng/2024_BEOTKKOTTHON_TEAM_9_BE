/**
 * 페이징 처리 JS
 * paging: {
        "pagePerCnt": 100,
        "totalPages": 1,
        "pageNo": 1,
        "totalCnt": 7,
        "hasNext": false
    }
 * targetFunction : 페이지 번호 클릭 시 호출할 함수
 */
const PagingUtil = {
    countPage : 10,

    pagingView: function (pagingObj, targetFunction) {
        const currentPageNo = pagingObj.pageNo;
        const pageSize = pagingObj.pagePerCnt;
        const totalCount = pagingObj.totalCnt;

        const pageNo = parseInt(currentPageNo); // 현재 페이지 번호

        let finalPageNo = parseInt(totalCount / pageSize);  //마지막 페이지 번호
        let strPaging = "";
        if (targetFunction == null || targetFunction == "") {
            targetFunction = "goPage";
        }

        if (totalCount % pageSize > 0) {
            finalPageNo++;
        }

        if (finalPageNo < pageNo) {
            pageNo = finalPageNo;
        }

        let startPage = Math.floor(((pageNo - 1) / this.countPage)) * this.countPage + 1;
        let endPage = startPage + this.countPage - 1;

        if (startPage <= 0) {
            startPage = 1;
        }
        if (endPage > finalPageNo) {
            endPage = finalPageNo;
        }

        //strPaging ="<ul class='paginate'>";
        if (pageNo > 1) {
            // strPaging += "<a href=\"javascript:"+targetFunction+"(1);\">처음으로</a>";
            strPaging += "<li class='page-item'><a href=\"javascript:" + targetFunction + "(" + (pageNo - 1) + "); \" class='page-link'><</a></li>";
        }

        for (var iCount = startPage; iCount <= endPage; iCount++) {
            if (iCount == pageNo) {
                strPaging += "<li class='page-item active'><a href='javascript:void(0);' class='page-link'><strong>" + iCount + "</strong></a></li>";
            } else {
                strPaging += "<li class='page-item'><a href=\"javascript:" + targetFunction + "(" + iCount + ");\" class='page-link'>" + iCount + "</a></li>";
            }
        }

        if (pageNo < finalPageNo) {
            strPaging += "<li class='page-item'><a href=\"javascript:" + targetFunction + "(" + (pageNo + 1) + ");\" class='page-link'>></a></li>";
            //strPaging += "<a href=\"javascript:"+targetFunction+"("+finalPageNo+"); \" class='next'>다음페이지</a>";
        }
        //strPaging +="</ul>";

        return strPaging;
    }

    // 시작 인덱스번호
    , getStartOffset: function (currentPageNo, pageSize) {
        //      return ((currentPageNo -1) * pageSize) + 1;
        return (currentPageNo - 1) * pageSize;
    }

    //끝 인덱스번호
    , getEndOffset: function (currentPageNo, pageSize) {
        return currentPageNo * pageSize;
    }

    //마지막 페이지 번호
    , getfinalPageNo: function (totalCount, pageSize) {
        var finalPageNo = parseInt(totalCount / pageSize);
        if (totalCount % pageSize > 0) {
            finalPageNo++;
        }
        return finalPageNo;
    }
}