from locust import HttpUser, task, between
import random

# 테스트용 accessToken을 계정별로 미리 준비 (실제 JWT나 세션값)
TOKENS = {
    "user2": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjMwLCJlbWFpbCI6InRlc3QyQG5hdmVyLmNvbSIsImlhdCI6MTc1MTYxNjIzMCwiZXhwIjoxNzUxNjI3MDMwfQ.T9EJVzC3tvVRuX6UUZXZNIXDH4vRmQFSlVFqIj2jsDE",
    "user3": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjMxLCJlbWFpbCI6InRlc3QzQG5hdmVyLmNvbSIsImlhdCI6MTc1MTYxNjI0NiwiZXhwIjoxNzUxNjI3MDQ2fQ.iodGRK9n8v62oOu9sGR__RC5ENiVUWMfFeJyffmBioY",
    "user4": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjMyLCJlbWFpbCI6InRlc3Q0QG5hdmVyLmNvbSIsImlhdCI6MTc1MTYxNjI2MiwiZXhwIjoxNzUxNjI3MDYyfQ.Ux6beXO75EbUtA-jk48s90k9SJkjS86p370WtiLgy0Y",
}

GROUP_ID = 2

class GroupJoinUser(HttpUser):
    wait_time = between(1, 2)  # 요청 간 간격

    def on_start(self):
        # 실행될 때 내 user 번호에 맞는 token 선택
        token = random.choice(list(TOKENS.values()))
        self.client.cookies.set("accessToken", token)

        self.joined = False  # 처음엔 참여하지 않은 상태로 시작

    @task
    def participate_or_cancel(self):
        # 아직 참여하지 않았다면 → 참여 요청
        if not self.joined:
            res = self.client.post(f"/api/v2/groups/{GROUP_ID}/applications")
            if res.status_code == 200:
                print("[참여] 성공")
                self.joined = True  # 참여 상태로 변경
            else:
                print(f"[참여] 실패: {res.status_code}")

        # 이미 참여 상태라면 → 참여 취소 요청
        else:
            res = self.client.delete(f"/api/v2/groups/{GROUP_ID}/applications")
            if res.status_code == 200:
                print("[취소] 성공")
                self.joined = False  # 다시 참여 안 한 상태로 변경
            else:
                print(f"[취소] 실패: {res.status_code}")