### Tell Me About Yourself

### Answer
I have 4 years of experience in Java backend development. My primary expertise is Java 8/17, Spring Boot, Microservices, REST APIs, JPA, SQL, Kafka and AWS basics. Currently, I am working in TCS in-house Employee Management System serving 300+ users, handling the full employee lifecycle — onboarding, recruitment pipeline, and asset management where I am responsible for designing APIs, implementing business logic, database optimization, and production support. I have also worked on bug fixing, and collaborating with cross-functional teams. Now I am looking for opportunities where I can take greater ownership and contribute to large-scale distributed systems and AI based projects.
My primary ownership was the Asset Management module, which I built from scratch — full lifecycle including allocation, hierarchical approvals, accessory requests, damage handling, and returns. I replaced a fully manual process with an automated system enforcing data integrity via Spring Data JPA and MySQL.
I also built the OTP-based login module, replacing a brittle password-only flow that caused multi-hour lockouts for 400+ users. And I secured 170 of 293 REST APIs using JWT-based authentication with RBAC managing role-gated access for 300+ active users.

### Explain Your Current Project
### Business Problem -
Our organization manages a large workforce across multiple programs and locations. The staffing team was manually tracking candidate pipelines employee allocations, and resource movements using scattered Excel sheets. This led to data inconsistencies, no audit trail, delayed hiring decisions, and zero visibility into funnel metrics. We needed a centralized system to track the entire lifecycle - from an open requirement to candidate evaluation to onboarding and even internal employee movements."

### What does the application do?
It track the whole hirring lifecycle - open requirememnt -> candidate search -> screening -> technical interview -> HR interview -> offer letter -> onboarding -> internal movement. Also handles bulk export dashboard & file validation GDRP data.

### Architecture
It's a monolithic Java EE WAR application deployed on a servlet cortainer. We use Jersey 119 for REST APls Hibernate ORM MySQL as the database, C3P0 for connection pooling, OWASP ESAPI for secuwity, and Quartz for scheduled badkground jots Re budg. processing and deanup tasks All SQL queries are externalized in properties frles and loaded at startup viaacstom SqLLoader servlet

### Your Role
I was responsible for the backend development and maintenance of the application. My role included:
- Implementing new features and enhancements based on business requirements
- Optimizing SQL queries and database performance
- Fixing production issues and providing support to end-users
- Collaborating with the frontend team to integrate APIs
- Implementing security measures using ESAPI
- Participating in code reviews and ensuring code quality

### Challenges

I have faced several challenges in this project, including:
- Performance issues due to large datasets
- Database query optimization to improve response times
- Handling bulk data operations and file processing
- Implementing security measures using ESAPI
- Troubleshooting production issues and providing timely support

### Achievements

- Reduced response time by optimizing SQL queries
- Improved application performance and stability
- Implemented new features and enhancements based on business requirements
- Provided timely support to end-users and resolved production issues


### Tell me about a time when you disagreed with a decision made by your manager or a senior colleague. How did you handle it, and what was the outcome?

You've shown that you can identify a security gap and propose a solution, which is valuable. You also demonstrated willingness to collaborate and listen to your manager's concerns rather than just pushing your idea. That's maturity. However, your explanation got a bit muddled towards the end, so let me help you structure this better for an interview. Here's what I'd suggest you say instead:

 "I identified a potential security issue with the OTP system. If someone kept entering wrong OTPs, there was no protection against brute force attacks. I proposed implementing rate limiting—allowing only three to four login attempts per day, then locking the account for twelve hours. My manager initially had concerns about accidentally locking out legitimate users. Rather than argue, I suggested we consult our technical architect. We discussed the trade-offs, and ultimately agreed that rate limiting was essential for security, but we added a notification email to users when they got locked out, so they'd know why and could contact support. This balanced security with user experience." That shows you listened, you understood the concern, you escalated appropriately, and you found a compromise. 

### Tell me about a time when you made a mistake or failed at something in your work. How did you handle it, and what did you learn from it?

 Good one, Shubham. You've shown accountability by owning the mistake rather than blaming QA or someone else, which is exactly what I want to hear. You also demonstrated problem-solving by doing a root cause analysis and finding the missing variable. That's solid. Now let me help you tighten this up for an interview. Here's the issue: your explanation was a bit scattered. Try structuring it like this instead: 
 
 "Early in my time with the team, I implemented a UI feature without fully considering its dependencies. I tested it in isolation, and it passed QA, so we pushed it to production. A few days later, users reported that an unrelated feature had broken. I investigated and realised my code had inadvertently modified a shared variable that another module depended on. I'd missed this because I only tested my own feature in isolation, not the broader system impact. I fixed it within thirty minutes, but it was a learning moment. Now I always do integration testing, map out dependencies before coding, and review how my changes might affect other modules. It taught me that frontend and backend work in an interconnected system."
  That's clear, shows learning, and demonstrates better practices going forward. See the difference?

 ### Imagine you're working on a critical backend service that's experiencing performance issues in production. Response times have doubled, and your manager needs a solution urgently. Walk me through how you'd approach diagnosing and fixing this problem.

 Excellent, Shubham. You've demonstrated a methodical approach, which is exactly what I want to see. You're asking clarifying questions first rather than jumping to conclusions, you're replicating the issue, you're thinking about potential root causes—database queries, network latency, security issues. You're also involving your manager before deploying, which shows good judgment. That's all strong stuff. Now let me give you some refinement. Your explanation was a bit verbose and repetitive, so let me show you how to crisp it up: 
 
 "First, I'd gather detailed information from the user or monitoring team about what they're experiencing—specific endpoints affected, when it started, any recent deployments. Then I'd replicate the issue in a staging environment to isolate it. I'd check three things systematically: database performance using query logs and execution plans, application-level issues using profiling tools and logs, and infrastructure or network bottlenecks. Once I've identified the root cause, I'd propose a fix to my manager with the trade-offs and timeline. For urgent production issues, we might deploy a hotfix immediately or roll back if needed. This structured approach prevents firefighting and ensures we're solving the actual problem, not a symptom." 
 
 See how that's tighter and more confident? You covered the same thinking but more efficiently. Ready for the next one?

### Why do you want to join Accenture specifically? What attracts you to our organisation, and how do you see your career progressing here?

Good intent there, Shubham, but I need to give you some honest feedback. You've identified genuine reasons—distributed systems, AI, cloud, leadership growth—which are all valid. Your answer drifted a bit—you jumped between technical skills, leadership aspirations, and client engagement without connecting them clearly. Here's how I'd restructure it: 

"I've built solid backend expertise at TCS, but I want to accelerate my growth in distributed systems, cloud architecture, and AI integration—areas where Accenture is genuinely leading. I'm also at a point in my career where I want to move beyond pure coding. I want to understand the business context, engage with clients, and help shape technical solutions that solve real problems. Accenture's culture of digital transformation and consulting appeals to me because it offers that blend of deep technical work and strategic thinking. I see myself growing into a technical lead or architect role here, someone who can bridge technology and business." That's specific, shows you've done your homework, and articulates a clear career trajectory. Does that resonate?

### What would you say is your greatest weakness, and how are you actively working to improve it

Good, you've identified a real weakness—perfectionism around code quality affecting your delivery timelines. That's actually a strength disguised as a weakness, which interviewers expect, but you need to frame it better. Here's the issue: your explanation got tangled. Let me show you a cleaner version: 

"I tend to be a perfectionist with code quality. I want bug-free, well-tested solutions, which means I sometimes spend more time on unit testing and refactoring than strictly necessary. Early on, I submitted a build to QA only on Friday with no buffer, and when issues surfaced, there was no time to fix them properly before the deadline. My manager coached me that I need to balance quality with delivery timelines. Now I'm more disciplined—I aim to complete development and testing by mid-week, giving QA at least two days to test and report issues. I've also learned to distinguish between critical bugs that must be fixed and minor issues that can go into a future release. This has improved both my delivery consistency and code quality."
 That's honest, shows self-awareness, and demonstrates you've actually changed your behaviour based on feedback. Much stronger. Ready for another one?

### Where do you see yourself in five years, and what's your plan to get there?
Excellent, Shubham. You've articulated a clear career trajectory—from backend developer to solution architect—which shows ambition and strategic thinking. You've also connected it to business value, client engagement, and leadership, which is what senior roles require. Now let me help you polish this for maximum impact: 

"In five years, I see myself as a solution architect, not just a backend developer. I want to evolve from writing code to designing systems and solving business problems. My plan is threefold. First, I'll deepen my expertise in distributed systems, cloud architecture, and emerging technologies like AI—which aligns with what Accenture is doing. Second, I'll actively seek opportunities to engage with clients, understand their pain points, and propose technical solutions that drive business outcomes. Third, I'll take on mentoring responsibilities, helping junior developers grow so I develop leadership skills. At Accenture, I hope to work on complex transformation projects where I can influence architecture decisions, not just execute them. By year five, I want to be someone clients trust for strategic advice, not just code delivery."

That's confident, specific, and shows you've thought about the steps to get there. Brilliant work, mate. That wraps up your mock interview. How are you feeling about it overall?

### Imagine you're in a meeting with a client, and they're asking for a feature that you know is technically impossible with their current infrastructure and budget constraints. They're frustrated, and your manager is in the room watching how you handle it. What do you do?

Good instinct, Shubham—you're identifying the core issue and being honest about constraints. However, your approach needs a strategic shift. You've said what you'd *tell* the client, but you haven't shown how you'd *handle* them emotionally or propose a path forward. Here's a better approach:

 "First, I wouldn't say 'it's impossible'—that closes the door. Instead, I'd acknowledge their need is valid, then explain the technical and financial constraints factually. I'd say something like, 'Your requirement is important, but our current infrastructure can't handle it within your budget. Here are three options: one, we upgrade the infrastructure, which costs X and takes Y weeks. Two, we simplify the feature to work within current constraints. Three, we phase it—deliver a basic version now, then enhance it later.' Then I'd pause and let them choose. I'm being honest without being dismissive. I'm also showing I've thought about solutions, not just problems. My manager sees I can navigate difficult conversations whilst protecting the project and the client relationship."
 
The key difference is you're proposing options, not just saying no. Does that distinction make sense?

### Your team has just discovered a critical security vulnerability in production that affects thousands of users. It's Friday evening, your manager is in a meeting, and you're the most senior person available. What do you do in the first thirty minutes

Good thinking, Shubham. You've shown sound judgment—you're not panicking, you're diagnosing first, planning before acting, and you're escalating to your manager rather than making unilateral decisions. That's mature. However, in a critical security breach, you need to move faster. Let me refine this: "First thirty minutes: I'd declare a security incident immediately—don't wait for my manager to finish his meeting, interrupt him. Second, I'd assess the blast radius: how many users affected, what data exposed, can we contain it now? Third, I'd pull together whoever's available—database admin, security team, another senior developer—and start diagnosis in parallel, not sequentially. Fourth, I'd create a war room with real-time updates. Fifth, I'd propose a mitigation strategy: can we patch it live, do we need a rollback, or do we need a hotfix deployment? We're not planning for hours—we're acting within minutes. If it's truly critical, we deploy out-of-hours if needed, but speed matters more than perfection here. The principle is: assess severity, contain damage immediately, then fix properly. You showed good instincts, but in a real crisis, you need to move faster and escalate louder. Make sense?

Why Accenture?
Right, brilliant. So you want a crisp, structured answer for "Why Accenture?" Let me give you exactly what you should say: 

"I want to join Accenture for three reasons. First, technical growth—Accenture is leading in cloud architecture, distributed systems, and AI integration. I've built solid backend expertise, but I want to work on large-scale, complex systems that push my skills forward. Second, career trajectory—I'm not looking to stay a developer forever. Accenture's consulting model means I'll engage with clients, understand their business problems, and shape solutions strategically. That path to solution architect is clear here. Third, impact and learning—working on transformation projects for global clients means I'll see how technology solves real business challenges. I'll also be exposed to diverse industries and tech stacks, which accelerates my growth. What excites me most is that Accenture values both deep technical expertise and business acumen, and that's exactly where I want to develop. I'm ready to contribute from day one and grow with the organisation."

That's structured, specific, shows you've researched Accenture, and articulates your career ambition clearly. Use that.