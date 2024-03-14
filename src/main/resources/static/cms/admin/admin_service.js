/**
 * 공통 코드 데이터 관리
 */
MbrService = {
    pagePerCnt : 5,
    /**
     * 회원목록조회
     */
    loadList: function(pageNo) {
        // 검색조건
        const jsonString = {
            pageNo: pageNo,
            pagePerCnt: this.pagePerCnt,
            deptId: $("#searchDeptId").val(),  // 부서 코드
            useYn: $("#searchUseYn").val(),  // 사용 여부
        };

        fetch("/cms/admin/user/list?"+ConvertUtil.jsonToQueryString(jsonString), {
            method : "GET",
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            if (data.code === "0000") {
                MbrUi.setMbrData(data.result, data.paging, pageNo);
                MbrUi.printList();
            }
            else {
                alert("조회 중 오류가 발생했습니다. " + data.code + " : " + data.message);
            }
        })
        .catch(error => console.log(`error => ${error}`));
    },
    /**
     * 회원정보 수정
     */
    modify: function() {
        const param = {
            mbrNo: $("#mbrNo").val(),
            mbrEmail : $("#mbrEmail").val(),
            mbrNickname: $("#mbrNickname").val(),
            verifiedYn: $("#verifiedYn").val(),
            profileImg: $("#profileImg").val(),
            mbrStatusCd: $("#mbrStatusCd").val(),
            mbrRoleCd: $("#mbrRoleCd").val()
        };

        fetch("/cms/admin/user", {
            method : "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            if (data.code === "0000") {
                alert("수정 되었습니다.");
                MbrService.loadList(MbrUi.curPageNo);
                MbrUi.closeModifyModal();
            }
            else {
                alert("수정 중 오류가 발생했습니다. " + data.code + " : " + data.message);
            }
        })
        .catch(error => console.log(`error => ${error}`));

    }

}

