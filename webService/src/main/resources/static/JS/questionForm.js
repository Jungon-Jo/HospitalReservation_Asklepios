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
