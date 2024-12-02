let customOverlay = new kakao.maps.CustomOverlay({
  clickable: false,
  xAnchor: 0.5,
  yAnchor: 1,
  zIndex: 3
});
let mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
      center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
      level: 3 // 지도의 확대 레벨
    };

let map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
// 마커를 담을 배열입니다
let markers = [];
let infowindow = new kakao.maps.InfoWindow({zIndex:1});

let geocoder = new kakao.maps.services.Geocoder();
let startFlag = false;
const ps = new kakao.maps.services.Places();
function getLocation(){
  // HTML5의 geolocation으로 사용할 수 있는지 확인합니다
  if (navigator.geolocation) {

    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
    navigator.geolocation.getCurrentPosition(function(position) {

      let lat = position.coords.latitude, // 위도
          lon = position.coords.longitude; // 경도

      let locPosition = new kakao.maps.LatLng(lat, lon), // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
          message = '<div class="customoverlay"><a><span class="title" style="font-size:20px;">여기에 계신가요?!</span></a></div>'; // 인포윈도우에 표시될 내용입니다

      // 마커와 인포윈도우를 표시합니다

      map.setCenter(locPosition);
      displayMarker(locPosition, message);
      // let currentLoc = geocoder.coord2RegionCode(lat, lon, callback); <-- callback error
      // console.log(currentLoc);
    });
  } else {
    // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다
    let locPosition = new kakao.maps.LatLng(33.450701, 126.570667),
        message = 'geolocation을 사용할수 없어요..'

    displayMarker(locPosition, message);
  }


  // 지도에 마커와 인포윈도우를 표시하는 함수입니다
  function displayMarker(locPosition, message) {

    // 마커를 생성합니다
    let marker = new kakao.maps.Marker({
      map: map,
      position: locPosition
    });

    let iwContent = message, // 인포윈도우에 표시할 내용
        iwRemoveable = false;

    // 인포윈도우를 생성합니다
    // let infowindow = new kakao.maps.InfoWindow({
    //   content : iwContent,
    //   removable : iwRemoveable
    // });

    // 커스텀 오버레이로 메세지를 표시
    customOverlay.setMap(map);
    customOverlay.setPosition(locPosition);
    customOverlay.setContent(message);

    // 인포윈도우를 마커위에 표시합니다
    // infowindow.open(map, marker);

    // 지도 중심좌표를 접속위치로 변경합니다
    map.setCenter(locPosition);
    map.setLevel(2);
    // let center = map.getCenter();
    // let loadAddr = geocoder.coord2RegionCode(locPosition.getLat(), locPosition.getLng());
    // console.log(loadAddr);
    // ps.keywordSearch(loadAddr, placesSearchCB);
    // 주소-좌표 변환 객체를 생성합니다
  }

}

// function addressSearch(){
//   let keyword = document.getElementById('search').value;
//   console.log(keyword);
//   if((!keyword)){
//     alert('검색어를 입력해주세요.');
//     return;
//   }else{
//     if(!(keyword.includes('병원'))){
//       keyword += '병원';
//     }
//   }
//   ps.keywordSearch(keyword, placesSearchCB);
//   if(startFlag){
//     removeAllChildNods(hospitalList);
//   }else{
//     startFlag = true;
//   }
// }

// function placesSearchCB(data, status, pagination) {
//   if (status === kakao.maps.services.Status.OK) {
//     // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
//     // LatLngBounds 객체에 좌표를 추가합니다
//     let bounds = new kakao.maps.LatLngBounds();

//     for (let i=0; i<data.length; i++) {
//       if(data[i].category_group_code === 'HP8' /*|| data[i].category_group_code === 'PM9'*/){
//         displayMarker(data[i], data[i].y, data[i].x);
//         addToHospitalList(data[i])
//         bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
//       }
//     }
//     if(bounds.isEmpty()){
//       alert('검색된 병원이 없습니다.');
//     }else{
//       // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
//       map.setBounds(bounds);
//     }
//   } else{
//     alert('검색 결과가 없습니다.');
//   }
// }

// // 지도에 마커를 표시하는 함수입니다
// function displayMarker(place, y, x) {
//   // 마커 이미지 생성
//   const imgSrc = "../static/hospital_icon.png",
//     imgSize = new kakao.maps.Size(65, 69),
//     imgOption = {offset: new kakao.maps.Point(27,69)};

//   const markerImage = new kakao.maps.MarkerImage(imgSrc, imgSize, imgOption);
//   // 글씨 이쁘게 바꾸기 위해 참고하려고 가져온 것
//   // const content = '<div class="customoverlay">' +
//   //               '<a href="https://map.kakao.com/link/map/11394059" target="_blank">' +
//   //               '<span class="title">구의야구공원</span>' +
//   //               '</a>' +
//   //               '</div>';
//                 // 마커를 생성하고 지도에 표시합니다
//   const marker = new kakao.maps.Marker({
//     map: map,
//     position: new kakao.maps.LatLng(place.y, place.x),
//     image: markerImage
//   });

//   // 마커에 클릭이벤트를 등록합니다
//   kakao.maps.event.addListener(marker, 'click', function() {
//     // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다
//     infowindow.setContent('<div style="padding:5px;font-size:12px;">' +
//       '<a href="https://map.kakao.com/link/to/'+ place.place_name + ',' + place.y + ',' + place.x +
//       '" style="color:blue" target="_blank">길찾기</a></div>'+ place.place_name + '</div>'
//     );
//     infowindow.open(map, marker);
//   });
// }

// 병원 리스트에 추가하는 함수
// function addToHospitalList(place) {
//     const hospitalList = document.getElementById('hospital-list');
//     const listItem = document.createElement('li');
//     listItem.innerHTML = `
//         <strong>${place.place_name}</strong><br>
//         ${place.address_name}<br>
//         <a href="https://map.kakao.com/link/to/${place.place_name},${place.y},${place.x}"
//           style="color:blue" target="_blank">길찾기</a>
//     `;
//     hospitalList.appendChild(listItem);
// }
//   // 검색결과 목록의 자식 Element를 제거하는 함수입니다
//   function removeAllChildNods(el) {
//     while (el.hasChildNodes()) {
//         el.removeChild (el.lastChild);
//     }
// }
// 키워드로 장소를 검색합니다
// addressSearch();

// 키워드 검색을 요청하는 함수입니다
function addressSearch() {
  let keyword = document.getElementById('search').value;

  if (!keyword.replace(/^\s+|\s+$/g, '')) {
    Swal.fire({
      icon: 'warning',
      title: '검색 실패 !',
      text: '검색할 주소를 입력해주세요.',
      confirmButtonColor: '#3B82F6',
      confirmButtonText: '확인'
    });
    return false;
  }

  if(!(keyword.includes('병원'))){
    keyword += '병원';
  }

  // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
  ps.keywordSearch(keyword, placesSearchCB, {size : 6});
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
  if (status === kakao.maps.services.Status.OK) {

    // 정상적으로 검색이 완료됐으면
    // 검색 목록과 마커를 표출합니다
    displayPlaces(data);

    // 페이지 번호를 표출합니다
    displayPagination(pagination);

  } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
    Swal.fire({
      icon: 'warning',
      title: '검색 실패 !',
      text: '검색 결과가 존재하지 않습니다.',
      confirmButtonColor: '#3B82F6',
      confirmButtonText: '확인'
    });
    return;

  } else if (status === kakao.maps.services.Status.ERROR) {
    Swal.fire({
      icon: 'erro',
      title: '검색 실패 !',
      text: '검색 결과 중 오류가 발생했습니다.',
      confirmButtonColor: '#3B82F6',
      confirmButtonText: '확인'
    });
    return;
  }
}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {

  var listEl = document.getElementById('hospitalList'),
      menuEl = document.getElementById('menu_wrap'),
      fragment = document.createDocumentFragment(),
      bounds = new kakao.maps.LatLngBounds(),
      listStr = '';

  // 검색 결과 목록에 추가된 항목들을 제거합니다
  removeAllChildNods(listEl);

  // 지도에 표시되고 있는 마커를 제거합니다
  removeMarker();

  for (let i=0; i<places.length; i++ ) {

    // 마커를 생성하고 지도에 표시합니다
    let latitude = places[i].y; // 위도
    let longitude = places[i].x; // 경도
    let placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
        marker = addMarker(placePosition, i),
        itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

    // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
    // LatLngBounds 객체에 좌표를 추가합니다
    bounds.extend(placePosition);

    // 마커와 검색결과 항목에 mouseover 했을때
    // 해당 장소에 인포윈도우에 장소명을 표시합니다
    // mouseout 했을 때는 인포윈도우를 닫습니다
    (function(marker, title) {
      kakao.maps.event.addListener(marker, 'click', function() {
        displayInfowindow(marker, title, latitude, longitude);
      });

      // kakao.maps.event.addListener(marker, 'click', function() {
      //   infowindow.close();
      // });

      // onclick vs onmouseover => onclick으로 설정
      itemEl.onclick =  function () {
        displayInfowindow(marker, title, latitude, longitude);
        let moveLatLon = new kakao.maps.LatLng(latitude, longitude);

      };

      // itemEl.onmouseout =  function () {
      //   infowindow.close();
      // };

    })(marker, places[i].place_name);

    fragment.appendChild(itemEl);
  }

  // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
  listEl.appendChild(fragment);
  menuEl.scrollTop = 0;

  // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
  map.setBounds(bounds);
}

// 검색결과 항목을 Element로 반환하는 함수입니다
// Ajax를 사용해 DB에 있는 장소만 예약하기 활성화
function getListItem(index, places) {
  let flag;
  let el = document.createElement('div');
  el.tabIndex = 0;
  el.className = 'item focus:bg-blue-200';
  let itemStr = /* '<i class="fa-solid fa-house-chimney-medical fa-2xl markerbg"></i>' */
      '<li class="item">' + '<img src="hospital_li.png" alt="아이콘" class="markerbg" />' +
      // '<span class="markerbg marker_' + (index+1) + '"></span>' +
      '<div class="info">' + '<h2>' + places.place_name + '</h2>';

  if (places.road_address_name) {
    itemStr += '<span>' + places.road_address_name.substring(0,15) + '</span>' +
        '<span class="jibun gray">' +  places.address_name.substring(0,15)  + '</span>';
  } else {
    itemStr += '<span>' +  places.address_name.substring(0,15)  + '</span>';
  }
  itemStr += '<span class="tel">' + places.phone  + '</span>' + '</li>';
  itemStr += '<div class="button">';
  itemStr += '</div>';
  el.innerHTML = itemStr;

  let buttonContainer = el.querySelector('.button');
  $.ajax({
    url: '/asklepios/findHospitalName',
    type: 'post',
    data: {hospital_name: places.place_name},
    success:function(data){
      console.log(data)
      if (data){
        let reserveButton = document.createElement('button');
        reserveButton.innerHTML = 'class="'
        reserveButton.type = 'button';
        reserveButton.className =
            'my-auto d-flex w-36 h-16 rounded-lg font-semibold text-white text-3xl bg-blue-500 hover:bg-blue-600';
        reserveButton.textContent = '예약하기';

        // onclick으로 모달 구현
        reserveButton.onclick = function () {
          alert(`병원 이름: ${places.place_name}`); // 원하는 동작
        };

        buttonContainer.appendChild(reserveButton);
      }
    },
    error: function (){
      alert("왜 안될까?");
    }
  });
  return el;
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
  var imageSrc = 'hospital_marker.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
      imageSize = new kakao.maps.Size(65, 69),
      imgOptions =  {
        // spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
        // spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
        offset: new kakao.maps.Point(27, 69) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
      },
      markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
      marker = new kakao.maps.Marker({
        position: position, // 마커의 위치
        image: markerImage
      });

  marker.setMap(map); // 지도 위에 마커를 표출합니다
  markers.push(marker);  // 배열에 생성된 마커를 추가합니다

  return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
  for ( var i = 0; i < markers.length; i++ ) {
    markers[i].setMap(null);
  }
  markers = [];
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
  var paginationEl = document.getElementById('pagination'),
      fragment = document.createDocumentFragment();

  // 기존에 추가된 페이지번호를 삭제합니다
  while (paginationEl.hasChildNodes()) {
    paginationEl.removeChild (paginationEl.lastChild);
  }

  for (i=1; i<=pagination.last; i++) {
    let el = document.createElement('a');
    el.href = "#";
    el.innerHTML = i;

    if (i===pagination.current) {
      el.className = 'on';
    } else {
      el.onclick = (function(i) {
        return function() {
          pagination.gotoPage(i);
        }
      })(i);
    }

    fragment.appendChild(el);
  }
  paginationEl.appendChild(fragment);
}

// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
// 인포윈도우에 장소명을 표시합니다
function displayInfowindow(marker, title, latitude, longitude) {
  let content = '<div class="customoverlay">' +
      '<a href="https://map.kakao.com/link/to/'+ title + ',' + latitude + ',' + longitude +
      '"target="_blank"><span class="title">' + title + '</span></a></div>';
  let position = new kakao.maps.LatLng(latitude, longitude);

  customOverlay.setMap(null);
  customOverlay.setMap(map);
  customOverlay.setContent(content);
  customOverlay.setPosition(position);
  map.setCenter(position);
  map.setLevel(2);
  map.setMaxLevel(5);
  // infowindow.setContent(content);
  // infowindow.open(map, marker);
}

// 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {
  while (el.hasChildNodes()) {
    el.removeChild (el.lastChild);
  }
}