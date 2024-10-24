import Events from "../../pages/Events.jsx";
import { useEffect, useState } from "react";
import axios from "axios";
import { useLocation, useParams } from "react-router-dom";

function EventWrite() {
  const { eventId } = useParams();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const mode = queryParams.get('mode');

  const cancelBtn = () => window.location.href = "/";

  const [eventTitle, setEventTitle] = useState('');
  const [eventStartDate, setEventStartDate] = useState('');
  const [eventEndDate, setEventEndDate] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  let [maxPeople, setMaxPeople] = useState('');
  const [eventContent, setEventContent] = useState('');

  useEffect(() => {
    if (mode === 'update') {
      axios.get(`http://localhost:8080/event/updateEvent/${eventId}`)
        .then((res) => {
          const data = res.data;
          setEventTitle(data.eventTitle);
          setEventStartDate(data.startDate);
          setEventEndDate(data.endDate);
          setStartTime(data.startTime);
          setEndTime(data.endTime);
          setMaxPeople(data.maxPeople);
          setEventContent(data.eventContent);
        })
        .catch(error => {
          console.error('이벤트 정보를 불러오는데 실패했습니다. : ', error);
        });
    }
  }, [eventId, mode]);


  const writingHandler = (e) => {


    e.preventDefault();

    if (eventEndDate < eventStartDate) {
      alert('종료일이 시작일보다 빠를 수 없습니다.');
      return;
    }
    if (endTime < startTime) {
      alert('종료시간이 시작시간보다 빠를 수 없습니다.');
      return;
    }

    if (maxPeople == '') {
      maxPeople = 0;
    }

    if (eventEndDate >= eventStartDate && endTime >= startTime) {
      const formData = new FormData();
      formData.append('eventTitle', eventTitle);
      formData.append('eventContent', eventContent);
      formData.append('eventStartDate', eventStartDate);
      formData.append('eventEndDate', eventEndDate);
      formData.append('startTime', startTime);
      formData.append('endTime', endTime);
      formData.append('maxPeople', maxPeople);
      formData.append('userId', sessionStorage.getItem("userId"));
      formData.append('eventAccept', 1);

      const fileInput = document.getElementById('file');
      if (fileInput.files[0]) {
        formData.append('file', fileInput.files[0]);
      }

      if (mode === 'update') {
        axios.put(`http://localhost:8080/event/updateEvent/${eventId}`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
          .then(() => {
            alert('등록에 성공했습니다. 협회장 승인을 기다려주세요.');
            window.location.href = '/';
          })
          .catch(e => {
            alert('등록 실패!\n'+ e.message + '\n관리자에게 문의하세요.');
          });
      }
      else {
        axios.post('http://localhost:8080/event/write', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
          .then(() => {
            alert('등록에 성공했습니다. 협회장 승인을 기다려주세요.');
            window.location.href = '/';
          })
          .catch(e => {
            alert('등록 실패!\n'+ e.message + '\n관리자에게 문의하세요.');
          });
      }
    }
  }

  const getTodayDate = () => {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  const todayDate =  getTodayDate();

  return (
    <section>
      <Events/>
      <div className={'form-border'}>
        <form onSubmit={writingHandler}>
          <div className="mt-4 py-2">
            <label htmlFor={'event-title'} className={'form-label'}>행사제목</label>
            <input type={'text'} className={'form-control'} placeholder={'제목을 입력해주세요'} id={'event-title'} name={'eventTitle'} value={eventTitle} onChange={(e) => setEventTitle(e.target.value)} required/>
          </div>

          <div className={'d-flex py-2 justify-content-between'}>
            <div className="w-50 d-flex me-5">
              <div className={'w-50 me-3'}>
                <label htmlFor={'event-startdate'} className={'form-label'}>행사 시작기간</label>
                <input type={'date'} className={'form-control me-3'} placeholder={'행사 시작기간'} id={'event-startdate'} name={'eventStartDate'} value={eventStartDate} onChange={e => setEventStartDate(e.target.value)} required min={todayDate}/>
              </div>
              <div className={'w-50'}>
                <label htmlFor={'end-enddate'} className={'form-label'}>행사 종료기간</label>
                <input type={'date'} className={'form-control'} placeholder={'행사 종료기간'} id={'event-enddate'} name={'eventEndDate'} value={eventEndDate} onChange={e => setEventEndDate(e.target.value)} required min={todayDate}/>
              </div>
            </div>
            <div className="w-50 d-flex">
              <div className={'w-50 me-3'}>
                <label htmlFor={'start-time'} className={'form-label'}>행사 시작시간</label>
                <input type={'time'} className={'form-control me-3'} placeholder={'행사 시작시간'} id={'start-time'} name={'startTime'} value={startTime} onChange={e => setStartTime(e.target.value)} required/>
              </div>
              <div className={'w-50'}>
                <label htmlFor={'end-time'} className={'form-label'}>행사 종료시간</label>
                <input type={'time'} className={'form-control'} placeholder={'행사 종료시간'} id={'end-time'} name={'endTime'} value={endTime} onChange={e => setEndTime(e.target.value)} required/>
              </div>
            </div>
          </div>

          <div className={'d-flex py-2 justify-content-between'}>
            {/*<div className="w-50 me-5">*/}
            {/*  <label htmlFor={'visible-date'} className={'form-label'}>게시일</label>*/}
            {/*  <input type={'date'} className={'form-control'} placeholder={'게시일'} id={'visible-date'}/>*/}
            {/*</div>*/}
            <div className="w-100">
              <label htmlFor={'max-people'} className={'form-label'}>정원수(선택사항)</label>
              <input type={'number'} className={'form-control'} placeholder={'정원을 입력해주세요.(미입력 시 정원 초과로 인한 조기 모집 마감이 되지 않습니다.)'} id={'max-people'} name={'maxPeople'} value={maxPeople} onChange={e => setMaxPeople(e.target.value)}/>
            </div>
          </div>

          {/*<div className={'d-flex py-2 justify-content-between'}>*/}
          {/*  <div className="w-50 me-5">*/}
          {/*    <label htmlFor={'upload-date'} className={'form-label'}>작성일</label>*/}
          {/*    <input type={'datetime-local'} className={'form-control'} placeholder={'작성일'} id={'upload-date'}/>*/}
          {/*  </div>*/}
          {/*  <div className="w-50">*/}
          {/*    <label htmlFor={'event-writer'} className={'form-label'}>작성자</label>*/}
          {/*    <input type={'text'} className={'form-control'} placeholder={'작성자를 입력해주세요'} id={'event-writer'}/>*/}
          {/*  </div>*/}
          {/*</div>*/}

          {/*<div className={'d-flex py-2 justify-content-between'}>*/}
          {/*  <div className="w-50 me-5">*/}
          {/*    <label htmlFor={'upload-date'} className={'form-label'}>승인일자</label>*/}
          {/*    <input type={'datetime-local'} className={'form-control'} placeholder={'승인일자'} id={'upload-date'}/>*/}
          {/*  </div>*/}
          {/*  <div className="w-50">*/}
          {/*    <label htmlFor={'event-approver'} className={'form-label'}>승인자</label>*/}
          {/*    <input type={'text'} className={'form-control'} placeholder={'승인자를 입력해주세요'} id={'event-approver'}/>*/}
          {/*  </div>*/}
          {/*</div>*/}

          <div className={'mt-3'}>
            <label htmlFor={'event-content'} className={'form-label'}>내용</label>
            <textarea className={'form-control py-3'} rows="5" placeholder={'내용을 입력해주세요'} id={'event-content'} name={'eventContent'} value={eventContent} onChange={e => setEventContent(e.target.value)} required/>
          </div>

          {/* 파일등록 */}
          <div className="input-group mt-4">
            <input type={'file'} className={'form-control py-2'} id={'file'}/>
            <label htmlFor={'file'} className={'input-group-text'}>Upload</label>
          </div>

          {/* 버튼 */}
          <div className={'d-flex justify-content-end mt-5'}>
            <button type={'button'} className={'btn btn-outline-point me-2'} onClick={cancelBtn}>취소</button>
            <button type={'submit'} className={'btn btn-point'}>완료</button>
          </div>
        </form>
      </div>
    </section>
  )
}

export default EventWrite;