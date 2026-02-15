$ErrorActionPreference = "Stop"

function Test-Endpoint {
    param($Name, $Url, $Method, $Headers, $Body)
    Write-Host "Testing $Name..." -NoNewline
    try {
        $params = @{
            Uri = $Url
            Method = $Method
            ContentType = "application/json"
        }
        if ($Headers) { $params.Headers = $Headers }
        if ($Body) { $params.Body = $Body }

        $response = Invoke-RestMethod @params
        Write-Host " [OK]" -ForegroundColor Green
        return $response
    } catch {
        Write-Host " [FAILED]" -ForegroundColor Red
        Write-Host $_.Exception.Message
        if ($_.Exception.Response) {
             $reader = New-Object System.IO.StreamReader $_.Exception.Response.GetResponseStream()
             Write-Host $reader.ReadToEnd()
        }
        return $null
    }
}

$baseUrl = "http://localhost:8080/api"

# 1. Login Student
$studentBody = @{ email = "student@secureexam.com"; password = "student123" } | ConvertTo-Json
$studentAuth = Test-Endpoint "Student Login" "$baseUrl/auth/login" "POST" $null $studentBody
$studentToken = $studentAuth.token
$studentId = $studentAuth.user.id
$studentHeaders = @{ Authorization = "Bearer $studentToken" }

# 2. Login Admin
$adminBody = @{ email = "admin@secureexam.com"; password = "admin123" } | ConvertTo-Json
$adminAuth = Test-Endpoint "Admin Login" "$baseUrl/auth/login" "POST" $null $adminBody
$adminToken = $adminAuth.token
$adminHeaders = @{ Authorization = "Bearer $adminToken" }

# 3. Get Exams
$exams = Test-Endpoint "Fetch Exams" "$baseUrl/exam" "GET" $studentHeaders $null
if ($exams.Count -eq 0) { Write-Error "No exams found!" }
$examId = $exams[0].id
Write-Host "Found Exam ID: $examId"

# 4. Start Exam
$startUrl = "$baseUrl/attempt/start/$examId/$studentId"
$attemptData = Test-Endpoint "Start Exam" $startUrl "POST" $studentHeaders $null
$attemptId = $attemptData.attemptId
Write-Host "Started Attempt ID: $attemptId"

# 5. Submit Exam
$answers = @(
    @{ questionId = $attemptData.questions[0].id; selectedOption = "Paris" },
    @{ questionId = $attemptData.questions[1].id; selectedOption = "Mars" }
) | ConvertTo-Json -Depth 5

$submitUrl = "$baseUrl/attempt/$examId/$studentId"
$score = Test-Endpoint "Submit Exam" $submitUrl "POST" $studentHeaders $answers
Write-Host "Exam Submitted. Score: $score"

# 6. Admin: Get All Attempts
$allAttempts = Test-Endpoint "Admin: Get All Attempts" "$baseUrl/attempt/all" "GET" $adminHeaders $null
$foundAttempt = $allAttempts | Where-Object { $_.id -eq $attemptId }
if ($foundAttempt) {
    Write-Host "Admin sees attempt $attemptId with score $($foundAttempt.score) [OK]" -ForegroundColor Green
} else {
    Write-Host "Admin could not find attempt $attemptId [FAILED]" -ForegroundColor Red
}

# 7. Admin: Review Attempt
$review = Test-Endpoint "Admin: Review Attempt" "$baseUrl/attempt/$attemptId/review" "GET" $adminHeaders $null
if ($review.score -eq $score) {
    Write-Host "Review Data Matches Score [OK]" -ForegroundColor Green
} else {
    Write-Host "Review Data Mismatch [FAILED]" -ForegroundColor Red
}
