function recommendMedical() {
    let userMessage = document.getElementById("question_content").value.trim(); // 공백 제외한 사용자 입력값

    // 챗봇 응답 받기
    $.ajax({
        url: "/asklepios/api/medical/recommend",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({mainMessage: userMessage}),
        success: function(response) {
            // 추천된 진료과 드롭다운 변경
            let medicalDropdown = document.getElementById("question_medical");
            let validOptions = Array.from(medicalDropdown.options).map(opt => opt.value);

            if (validOptions.includes(response)) {
                medicalDropdown.value = response;
            } else {
                alert("추천된 진료과가 목록에 없습니다.");
            }
        },
        error: function() {
            alert("진료과 추천 요청 실패");
        }
    });
}

// tagVOList에 배열 i 생성
let i = 0;
// Hidden에 담을 문자열 생성
let tagBox = "";

function addTag(){
    let tagInput = document.querySelector("#write_tag").value.trim();
    let tagList = document.querySelector("#tag_list");
    let tag = document.createElement('span');
    tag.innerHTML = "<span style='font-size: 30px; background: #3b82f6; color: white; padding: 15px; margin: 10px; border-radius: 30px;' data-name='tagVOList[" + i + "].tag' data-value='" + tagInput + "'>#" + tagInput + "</span>" ;
    if(tagInput === ""){
        alert("태그를 작성해주세요.");
    }else{
        if(i >= 5){
            alert("작성 가능한 태그는 5개 입니다.")
        }else{
            if(i !== 0){
                tagBox += ",";
            }
            i++;
            tagList.appendChild(tag);
            document.querySelector("#tag_notice").style.display = 'none';
            document.querySelector("#write_tag").value = "";
            tagBox += tagInput;
            updateHiddenInput();
        }
    }

}
function updateHiddenInput() {
    let hiddenInput = document.querySelector("input[name='tag']");

    if (!hiddenInput) {
        hiddenInput = document.createElement("input");
        hiddenInput.type = "hidden";
        hiddenInput.name = "tag";
        document.querySelector("#tag_list").appendChild(hiddenInput);
    }

    hiddenInput.value = tagBox;
}
// function addTag(){
//     let tagInput = document.querySelector("#write_tag").value.trim();
//     let tagList = document.querySelector("#tag_list");
//     let tagCnt = tagList.childElementCount - 1;
//     if (tagInput === "") {
//         alert("태그를 작성해주세요.");
//         return;
//     }
//
//     if (tagCnt >= 5) {
//         alert("작성 가능한 태그는 5개 입니다.");
//         return;
//     }
//
//     // 태그 요소 생성
//     let tag = document.createElement('span');
//     tag.innerHTML = `<span style="font-size: 30px; background: #3b82f6; color: white; padding: 20px; margin: 10px; border-radius: 20px;">
//                         #${tagInput}</span>`;
//
//     // 태그 리스트에 추가
//     tagList.appendChild(tag);
//
//     // hidden input 추가 (VO에 저장될 형태)
//     let hiddenInput = document.createElement('input');
//     hiddenInput.type = 'hidden';
//     hiddenInput.name = 'tagVOList[' + tagCnt + '].name';
//     hiddenInput.value = tagInput;
//     tag.appendChild(hiddenInput);
//
//     // 태그 입력 필드 초기화
//     document.querySelector("#write_tag").value = "";
//     document.querySelector("#tag_notice").style.display = 'none';
// }

function validateForm(){
    let title = document.querySelector("#question_title").value;
    let content = document.querySelector("#question_content").value;
    let medical = document.querySelector("#question_medical").value;
    let formFlag = false;

    if(title === ""){
        Swal.fire('필수항목 미입력', '제목을 입력해주세요.','error');
        return formFlag;
    }
    if(content === ""){
        Swal.fire('필수항목 미입력', '내용을 입력해주세요.','error');
        return formFlag;
    }
    if(medical === ""){
        Swal.fire('필수항목 미입력', '진료과목을 선택해주세요.','error');
        return formFlag;
    }
    if(confirm("질문하시겠습니까?")){
        formFlag = true;
        return formFlag;
    }else {
        formFlag = false;
        return formFlag;
    }
}
