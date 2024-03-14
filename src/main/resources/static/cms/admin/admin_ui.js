MbrUi = {
    mbrList: [],
    paging: null,
    curPageNo: 1,
    setMbrData: function(mbrList, paging, pageNo) {
        this.mbrList = mbrList;
        this.paging = paging;
        this.curPageNo = pageNo;
    },
    printList: function() {
        const tbody = $("#user_list_table > tbody").empty();

        this.mbrList.forEach(function(mbr) {
            const td1 = $("<td>").text(mbr.userId);
            const td2 = $("<td>").text(mbr.userName);
            const td3 = $("<td>").text(mbr.deptName);
            const td4 = $("<td>").text(mbr.authorCd);
            const td5 = $("<td>").text(mbr.useYn);


            // const modifyBtn = $("<button>").text("수정")
            //                         .attr("onclick","MbrUi.openModifyModal("+mbr.mbrNo+")");
            // const td10 = $("<td>").append(modifyBtn);

            const trObj = $("<tr onclick=\"location.href=\'/cms/admin/user/" + mbr.userId + "\'\">")
                .append(td1).append(td2).append(td3).append(td4)
                .append(td5)
                // .append(td6).append(td7).append(td8)
                // .append(td9).append(td10)
            ;
            tbody.append(trObj);
        });

        if (this.paging !== null) {
            $("#mbrTotalCnt").text(this.paging.totalCnt);
            $("#pagination").html(PagingUtil.pagingView(this.paging, "MbrService.loadList"));
        }
    },

    closeModifyModal: function() {
        $("#modal_mbrDetail").modal('hide');
    },
    openModifyModal: function(mbrNo) {
        console.log("mbrNo : " + mbrNo);

        // 회원정보 조회
        //console.log(this.mbrList);
        const mbrObj = this.mbrList.find(function(e){
            if (e.mbrNo === mbrNo) return true;
        });

        if (mbrObj === undefined || mbrObj === null) {
            console.error("회원 데이터 조회 실패 : mbrNo = " + mbrNo);
        }

        console.log(mbrObj);

        $("#mbrNo").val(mbrObj.mbrNo);
        $("#mbrEmail").val(mbrObj.mbrEmail);
        $("#mbrNickname").val(mbrObj.mbrNickname);
        $("#verifiedYn").val(mbrObj.verifiedYn);
        $("#profileImg").val(mbrObj.profileImg);
        $("#point").val(mbrObj.point);
        $("#mbrStatusCd").val(mbrObj.mbrStatusCd).prop("selected", true);
        $("#mbrRoleCd").val(mbrObj.mbrRoleCd).prop("selected", true);

        $("#modal_mbrDetail").modal('show');
    }
}