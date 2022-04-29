# lightening-backend

## POST API

### <API URL>
  
  <em>url/posts</em> 전체 포스트 출력
  
  parameter "tag" 태그로 구분 
  
  <strong>MEAL, COFFEE, ALCOHOL, GAME, ETC ( 밥 커피 술 게임 기타 )</strong>
  
  parameter "type" 모집상태로 구분
  
  <strong>RECRUIT, COMPLETED ( 모집 / 모집환료 )</strong>
  
  parameter "page" "size" 페이징 관련 변수

### < Post >
Long postId; // 포스트 id<br>
Long accountId; // 작성자 pk id<br>
String accountUsername; // 작성자 아이디<br>
String accountNickname; // 작성자 닉네임<br>
String accountImage; <br>
PostTag postTag; // post 태그<br>
PostType postType; // post 모집완료상태<br>
List<EmotionDto> emotions; // 관심/참여 인원<br>
String postContent; // 내용<br>
Date meetDate; // 약속 시간<br>
Date recruitEndDate; // 게시 종료 시간<br>
int maxCount; // 최대 인원<br>
  
  
### < Emotion 참여자 >
Long accountId; // 참여자<br>
String accountUsername; // 참여자 아이디<br>
EmotionType emotionType; // 참여 타입(관심, 참가)<br>
  
