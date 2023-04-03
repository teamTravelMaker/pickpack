import {useState} from 'react';
import styled from "styled-components"

export default function Mypage(){
    const [mypageMode, setMypageMode] = useState(1);
    return(
        <div style={{display: 'flex'}}>
            <MypageLeft givenHeight={window.screen.availHeight - 88}>
                <LeftTitle>
                    <span>마이페이지</span>
                </LeftTitle>
                <LeftContent onClick={() => setMypageMode(1)} isFocused={mypageMode === 1 ? true : false}>
                    <span>항공권</span>
                </LeftContent>
                <LeftContent onClick={() => setMypageMode(2)} isFocused={mypageMode === 2 ? true : false}>
                    <span>여행용품</span>
                </LeftContent>
                <LeftContent onClick={() => setMypageMode(3)} isFocused={mypageMode === 3 ? true : false}>
                    <span>회원정보수정</span>
                </LeftContent>
            </MypageLeft>
            <div>
                <div>
                    <div style={{fontSize: '32px'}}>편도</div>
                    <div style={{fontSize: '32px'}}>왕복</div>
                </div>
                <div>
                    Content
                </div>
            </div>
        </div>
    )
}

const MypageLeft = styled.div`
    width: 320px;
    border: 1px solid black;
    background-color: #E9E7EF;
    height: ${(props) => props.givenHeight}px;
`;

const LeftTitle = styled.div`
    height: 144px;
    // border: 1px solid black;
    font-weight: bold;
    font-size: 32px;
    display: flex;
    justify-content: center;
    align-items: center;
`;

const LeftContent = styled.div`
    height: 72px;
    border-top: 1px solid black;
    font-weight: bold;
    font-size: 16px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: ${(props) => props.isFocused? '#D5D1E1' : 'none'};
    &:last-child {
        border-bottom: 1px solid black;
    }
    &:hover {
        cursor: pointer;
        background-color: #D5D1E1;
    }
`;