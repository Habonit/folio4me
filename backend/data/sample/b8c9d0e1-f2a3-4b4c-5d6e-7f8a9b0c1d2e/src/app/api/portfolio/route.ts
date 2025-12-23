import { NextResponse } from 'next/server'
import fs from 'fs'
import path from 'path'

export async function GET() {
  try {
    const filePath = path.join(process.cwd(), '..', 'portfolio.json')
    const data = fs.readFileSync(filePath, 'utf-8')
    return NextResponse.json(JSON.parse(data))
  } catch (error) {
    return NextResponse.json(
      { error: 'Failed to load portfolio data' },
      { status: 500 }
    )
  }
}
