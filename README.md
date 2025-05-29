Sure! Here's your `README.md` translated and formatted in English:

---

````markdown
# Media Distributor

**Media Distributor** is a backend microservice designed to handle the distribution and management of media files (images, videos, audio, documents, etc.) across the LexiLearn system.

## 📚 Table of Contentss

- [Introduction](#introduction)
- [Key Features](#key-features)
- [System Architecture](#system-architecture)
- [Project Structure](#project-structure)
- [Directory & Component Details](#directory--component-details)
- [AWS S3 Integration](#aws-s3-integration)
- [Usage Example](#usage-example)
- [Development & Deployment](#development--deployment)
- [Future Improvements](#future-improvements)

## 🔰 Introduction

The Media Distributor microservice acts as the central hub for media operations, allowing other services and clients to upload, retrieve, and manage media content via a unified API.

## 🌟 Key Features

- Upload and download files via REST API.
- Store files securely in AWS S3.
- Maintain metadata (owner, usage type, timestamp, etc.).
- Support for different usage types (e.g., profile images, lesson content, etc.).
- Scalable and extensible design for future use cases.

## 🏗️ System Architecture

```plaintext
+------------+      REST API       +------------------+
|  Frontend  | <-----------------> | Media Distributor |
+------------+                     +---------+--------+
                                              |
                                              | AWS SDK
                                              |
                                      +-------v--------+
                                      |     AWS S3     |
                                      +----------------+
```
````

- The frontend (e.g., LexiLearn web/app) interacts with the Media Distributor to manage media files.
- Media Distributor handles authentication, file processing, and communicates with AWS S3 for file storage.

## 📁 Project Structure

```bash
media-distributor/
├── src/
│   ├── config/         # Configuration (S3, environment variables, etc.)
│   ├── controllers/    # API request handlers
│   ├── services/       # Core business logic
│   ├── routes/         # Route definitions
│   ├── middlewares/    # Custom middleware (e.g., file validation)
│   ├── utils/          # Helper functions
│   └── index.ts        # Entry point
├── uploads/            # Temporary file storage (if needed)
├── .env                # Environment variables
├── package.json
└── README.md
```

## 🧩 Directory & Component Details

### `controllers/`

Defines logic for each endpoint: upload, download, delete, etc.

### `services/`

Contains core logic for interacting with AWS S3 and handling file metadata.

### `routes/`

Defines API endpoints, mapped to the appropriate controller logic.

### `middlewares/`

Includes middleware for request validation, file type checks, etc.

### `config/`

Holds configuration such as AWS credentials, region, and S3 bucket setup.

## ☁️ AWS S3 Integration

The service uses the AWS SDK to upload files to a designated S3 bucket. Files are typically organized by usage type or user ID.

Example S3 Path:

```
s3://lexilearn-media/profile/user123/avatar.png
```

Environment variables required:

```env
AWS_ACCESS_KEY_ID=your_access_key
AWS_SECRET_ACCESS_KEY=your_secret_key
AWS_REGION=your_region
S3_BUCKET_NAME=lexilearn-media
```

## 🚀 Usage Example

### Upload API

**Endpoint:** `POST /api/media/upload`
**Form Data:** `file`, `usage`, `ownerId`

**Response:**

```json
{
  "url": "https://s3.amazonaws.com/lexilearn-media/profile/user123/avatar.png",
  "fileName": "avatar.png",
  "usage": "PROFILE",
  "ownerId": "user123"
}
```

### Download API

**Endpoint:** `GET /api/media/:fileName`
**Returns:** Redirects or streams file content

## 🛠️ Development & Deployment

### Local Development

```bash
npm install
npm run dev
```

Ensure AWS credentials are properly configured in your `.env` file.

### Deployment

You can deploy this service using any containerized platform (e.g., Docker, Kubernetes, ECS).

## 🔮 Future Improvements

- Add support for CDN (e.g., CloudFront) for faster delivery.
- Integrate with message queues for async processing (e.g., image compression).
- Store media metadata in a database for better search and filtering.
- Add support for signed URLs and access control.

---

© 2025 LexiLearn. All rights reserved.

```

---

Let me know if you'd like to include Docker instructions, Swagger API docs, or diagrams/images in this `README.md` as well.
```
