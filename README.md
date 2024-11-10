# 기능 구현 목록

## 편의점 상품 목록 출력

- [ ] 프로덕션 파일로부터 상품 목록 데이터를 읽는다.
- [ ] 읽은 상품 목록 데이터를 출력한다.
  - [ ] 프로모션이 아예 없는 상품은 기본 재고만 출력한다.
  - [ ] 프로모션이 있는 상품은 기본 재고와 프로모션 재고를 모두 출력한다.
    - [ ] **프로모션의 기간이 지난 경우, 프로모션 재고를 출력하지 않는다.**
  - [ ] 재고가 0인 경우에는 `재고 없음`으로 출력한다.
- [ ] 구매로 인해 업데이트된 상품 목록을 프로그램 정상 종료시 **파일에 작성**한다.

## 구매 목록 입력

- [ ] 사용자로부터 구매할 상품 정보들을 입력받는다.
- [x] 입력을 검증한다.
  - [x] 하나씩의 대괄호로 열고 닫혀야 한다.
  - [x] 하나의 대시로 상품 이름과 개수가 분리되어야 한다.
  - [x] 상품 개수는 1개 이상이어야 한다.
- [ ] 구매할 상품 정보들과 편의점의 상품 목록을 대조하여 결제 가능 여부를 체크한다.
  - [ ] 동일한 상품을 2번 구매하려고 시도할 경우, 한번에 구매하도록 처리한다.
  - [ ] 각 구매 상품이 편의점에 존재하는 상품이어야 한다.
  - [ ] 각 구매 상품의 기본 재고 + 기간 내의 프로모션 재고가 구매량 이상이어야 한다.
- [ ] 각 상품의 결정 타입을 선택할 수 있다.
  - [ ] 프로모션 기간이 지났거나, 기본 재고만 존재할 때 : 그냥 구매한다.
  - [ ] 프로모션 기간 내이며, 프로모션 단위에 맞게 존재할 때 : 그냥 구매한다.
  - [ ] 프로모션 기간 내이며, 무료로 상품을 받을 수 있을 때 : 무료 상품을 가져올지 여부를 물어본다.
  - [ ] 프로모션 기간 내이며, 일부 수량을 프로모션 없이 결제해야 하는 경우 : 프로모션 미적용 개수만큼 가져놓을지 여부를 물어본다.
    - [ ] ex. 3+1인데 6개를 가져온 경우, 2개는 프로모션이 미적용되므로 물어봐야 한다.
    - [ ] ex. 3+1이고 재고가 9개인데, 9개를 가져온 경우, 1개는 프로모션이 미적용되므로 물어봐야 한다.

### 결정 타입 - 무료로 상품을 받을 수 있을 때

- [ ] 사용자로부터 무료 상품을 가져올지 입력받는다.
- [ ] 무료로 가져온다면 그만큼 구매 정보에 추가한다.

### 결정 타입 - 프로모션 미적용 상품이 있을 때

- [ ] 사용자로부터 미적용 개수만큼 가져놓을지 입력받는다.
- [ ] 가져다 놓는다면 그만큼 구매 정보에서 제외한다.

## 멤버십 적용

- [ ] 사용자로부터 멤버십 적용 여부를 입력받는다.
- [ ] 멤버십을 적용할 경우
  - [ ] 프로모션되지 못한 상품들에 한해서 30% 할인을 해준다.
    - [ ] ex. 3+1인데 10개를 가져온 경우, 8개는 프로모션 대상이므로 2개에 대해서만 적용한다.
  - [ ] 할인 금액은 최대 8000원이다.
- [ ] 멤버십을 적용하지 않을 경우
  - [ ] 0원 할인을 적용한다.

## 영수증 출력

- [ ] 위 결정들에 대해 구매내역을 확정하고, 정산한다.
- [ ] 구매한 상품 내역을 출력한다.
  - [ ] 프로모션을 포함한 총 구매내역을 출력한다.
    - [ ] 상품 이름, 총 구매 개수, 총 가격을 출력한다.
  - [ ] 프로모션 미적용 상품을 가져놓음으로 인하여 0개 구매했을 경우에는, `취소`로 표시한다.
- [ ] 증정 상품 내역을 출력한다.
  - [ ] 프로모션으로 인해 얻은 상품 내역을 출력한다.
    - [ ] 상품 이름, 증정 개수
    - [ ] ex. 3+1인데 9개를 구매했다면 2개 증정이 된다.
- [ ] 금액 정보를 출력한다.
  - [ ] 총구매액 : 기타 할인과 프로모션을 적용하지 않은 원래 금액
  - [ ] 행사할인 : 프로모션에 의해 할인된 금액
  - [ ] 멤버십할인 : 멤버십으로 의해 할인된 금액
  - [ ] 내실돈 : 총구매액에서 두 할인을 뺀, 최종 구매액

## 테스트 환경 설정

- [ ] 테스트 시에는 각 테스트가 종료되더라도 파일을 덮어쓰지 않는다.
- [ ] 테스트 시에는 프로덕션용 파일이 아닌, 테스트용 파일을 읽어서 사용한다.
